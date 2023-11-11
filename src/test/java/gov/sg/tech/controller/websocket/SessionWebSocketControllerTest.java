//package gov.sg.tech.controller.websocket;
//
//import gov.sg.tech.controller.transformer.SessionDataTransformer;
//import gov.sg.tech.domain.dto.JoinSessionRequest;
//import gov.sg.tech.domain.dto.SessionResponse;
//import gov.sg.tech.domain.dto.UserResponse;
//import gov.sg.tech.service.SessionService;
//import gov.sg.tech.util.ObjectMapperSingleton;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.Transport;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class SessionWebSocketControllerTest {
//
////    @Autowired
////    private SessionService sessionService;
////
////    @Autowired
////    private SessionDataTransformer sessionDataTransformer;
//
//    private int port;
//
//    private String url;
//
//    private CompletableFuture<SessionResponse> completableFuture;
//
//    @BeforeEach
//    void setUp () {
//        completableFuture = new CompletableFuture<>();
//        port = 8080;
//        url = "ws://localhost:" + port + "/restaurantPicker";
//    }
//
//    @SneakyThrows
//    @DisplayName("Test Join Session")
//    @Test
//    void testJoinSession() {
//        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
//
//        StompSession stompSession = stompClient.connectAsync(url,
//                new StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS);
//
//        stompSession.subscribe(String.format("/sessions/%s/join", 123), new CustomStompFrameHandler());
//        stompSession.send("/topic/sessions/manage", getStubSessionResponse());
//
//        SessionResponse sessionResponse = completableFuture.get(10, TimeUnit.SECONDS);
//
//        assertNotNull(sessionResponse);
//    }
//
//    private List<Transport> createTransportClient() {
//        List<Transport> transports = new ArrayList<>();
//        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
//
//        return transports;
//    }
//
//    private SessionResponse getStubSessionResponse() {
//        return SessionResponse.builder()
//                .sessionId(1234L)
//                .sessionName("Monday Lunch")
//                .users(Collections.singleton(UserResponse.builder()
//                        .userId(4L)
//                        .username("Jon Snow")
//                        .build()))
//                .build();
//    }
//
//    private class CustomStompFrameHandler implements StompFrameHandler {
//
//        @Override
//        public Type getPayloadType(StompHeaders headers) {
//            return SessionResponse.class;
//        }
//
//        @Override
//        public void handleFrame(StompHeaders headers, Object payload) {
//            completableFuture.complete((SessionResponse) payload);
//        }
//    }
//}
