/**
 * chat-room.js
 * 흐름: 방 확보 -> 초기 메시지 로딩 -> STOMP 연결 및 구독 -> 메시지 수신 시 UI 렌더링
 */
(function () {
    // UI 요소 선택자
    const otherEl = document.getElementById("otherUsername");
    const btnCreate = document.getElementById("btnCreateRoom");
    const roomIdEl = document.getElementById("roomId");
    const messagesEl = document.getElementById("messages");
    const inputEl = document.getElementById("messageInput");
    const btnSend = document.getElementById("btnSend");

    let roomId = null;
    let stompClient = null;
    let myUsername = null; // 현재 로그인한 사용자 이름 저장용

    // XSS 방지를 위한 HTML 이스케이프 함수
    const esc = (s) => {
        if (!s) return "";
        return String(s)
            .replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;").replaceAll("'", "&#039;");
    };

    // 메시지를 화면에 추가하는 함수 (디자인 적용)
    const appendMessage = (m) => {
        // '대화를 시작하세요' 문구가 있다면 제거
        const emptyState = document.querySelector('.empty-state');
        if (emptyState) emptyState.remove();

        const isMe = m.senderUsername === myUsername;
        const li = document.createElement("li");

        // UI 스타일 적용 (내가 보낸 건 오른쪽, 상대방은 왼쪽)
        li.style.display = "flex";
        li.style.flexDirection = "column";
        li.style.maxWidth = "70%";
        li.style.marginBottom = "15px";
        li.style.padding = "12px 16px";
        li.style.borderRadius = "15px";
        li.style.fontSize = "0.95rem";
        li.style.lineHeight = "1.4";
        li.style.position = "relative";

        if (isMe) {
            li.style.alignSelf = "flex-end";
            li.style.backgroundColor = "#2196F3"; // 메인 블루
            li.style.color = "white";
            li.style.borderBottomRightRadius = "2px";
        } else {
            li.style.alignSelf = "flex-start";
            li.style.backgroundColor = "#F0F0F0";
            li.style.color = "#333";
            li.style.borderBottomLeftRadius = "2px";
        }

        li.innerHTML = `
            <strong style="display:block; font-size:0.75rem; margin-bottom:4px; opacity:0.8;">
                ${isMe ? "나" : esc(m.senderUsername)}
            </strong>
            <div>${esc(m.content)}</div>
            <span style="display:block; font-size:0.7rem; margin-top:4px; opacity:0.6; text-align:right;">
                ${m.createdAt ? m.createdAt.split('T')[1].substring(0, 5) : "방금"}
            </span>
        `;

        messagesEl.appendChild(li);

        // 최신 메시지로 스크롤 이동
        messagesEl.scrollTop = messagesEl.scrollHeight;
    };

    // 1. 방 생성 또는 조회 (POST)
    const createOrGetRoom = async (otherUsername) => {
        const res = await fetch("/api/chat/room", {
            method: "POST",
            headers: { "content-type": "application/json" },
            body: JSON.stringify({ otherUsername }),
        });
        if (!res.ok) return null;
        return await res.json(); // {roomId, me, other} 반환
    };

    // 2. 과거 메시지 로딩 (GET)
    const loadRecentMessages = async (rid) => {
        const res = await fetch(`/api/chat/rooms/${rid}/messages?size=50`);
        if (!res.ok) return [];
        return await res.json();
    };

    // 3. STOMP 연결 및 구독
    const connectStomp = (rid) => {
        if (stompClient && stompClient.connected) {
            stompClient.disconnect();
        }

        const sock = new SockJS("/ws");
        stompClient = Stomp.over(sock);
        stompClient.debug = null; // 콘솔 로그 깔끔하게

        stompClient.connect({}, () => {
            console.log("STOMP Connected to room: " + rid);

            // 전역 채널 구독
            stompClient.subscribe(`/topic/chat/rooms/${rid}`, (frame) => {
                const payload = JSON.parse(frame.body);
                appendMessage(payload); // 서버에서 온 메시지만 화면에 그림
            });
        }, (err) => {
            console.error("STOMP Error: " + err);
        });
    };

    // 4. 메시지 전송
    const sendMessage = () => {
        if (!roomId || !stompClient || !stompClient.connected) return;

        const content = inputEl.value.trim();
        if (!content) return;

        // 서버 전송 (@MessageMapping으로 전달)
        stompClient.send(`/app/chat/rooms/${roomId}/send`, {}, JSON.stringify({ content }));

        inputEl.value = "";
        inputEl.focus();
    };

    // 이벤트 리스너 설정
    btnCreate.addEventListener("click", async () => {
        const other = otherEl.value.trim();
        if (!other) return alert("상대방 이름을 입력하세요.");

        const room = await createOrGetRoom(other);
        if (!room) return alert("방 생성 실패");

        roomId = room.roomId;
        myUsername = room.me; // 내 이름 저장
        roomIdEl.textContent = roomId;

        messagesEl.innerHTML = ""; // 화면 초기화
        const recent = await loadRecentMessages(roomId);
        recent.forEach(m => appendMessage(m));

        connectStomp(roomId);
    });

    btnSend.addEventListener("click", sendMessage);
    inputEl.addEventListener("keydown", (e) => {
        if (e.key === "Enter") sendMessage();
    });
})();