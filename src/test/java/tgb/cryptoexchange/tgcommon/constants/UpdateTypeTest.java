package tgb.cryptoexchange.tgcommon.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import tgb.cryptoexchange.tgcommon.exception.NoChatIdFromUpdateException;
import tgb.cryptoexchange.tgcommon.exception.TelegramCommonException;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTypeTest {

    // ===== fromUpdate =====

    @Nested
    @DisplayName("fromUpdate()")
    class FromUpdateTests {
        @Test
        void testMessage() {
            assertEquals(UpdateType.MESSAGE, UpdateType.fromUpdate(new Update() {
                {
                    setMessage(new Message());
                }
            }));
        }

        @Test
        void testInlineQuery() {
            assertEquals(UpdateType.INLINE_QUERY, UpdateType.fromUpdate(new Update() {
                {
                    setInlineQuery(new InlineQuery());
                }
            }));
        }

        @Test
        void testCallbackQuery() {
            assertEquals(UpdateType.CALLBACK_QUERY, UpdateType.fromUpdate(new Update() {
                {
                    setCallbackQuery(new CallbackQuery());
                }
            }));
        }

        @Test
        void testEditedMessage() {
            assertEquals(UpdateType.EDITED_MESSAGE, UpdateType.fromUpdate(new Update() {
                {
                    setEditedMessage(new Message());
                }
            }));
        }

        @Test
        void testChannelPost() {
            assertEquals(UpdateType.CHANNEL_POST, UpdateType.fromUpdate(new Update() {
                {
                    setChannelPost(new Message());
                }
            }));
        }

        @Test
        void testEditedChannelPost() {
            assertEquals(UpdateType.EDITED_CHANNEL_POST, UpdateType.fromUpdate(new Update() {
                {
                    setEditedChannelPost(new Message());
                }
            }));
        }

        @Test
        void testShippingQuery() {
            assertEquals(UpdateType.SHIPPING_QUERY, UpdateType.fromUpdate(new Update() {
                {
                    setShippingQuery(new ShippingQuery());
                }
            }));
        }

        @Test
        void testPreCheckoutQuery() {
            assertEquals(UpdateType.PRE_CHECKOUT_QUERY, UpdateType.fromUpdate(new Update() {
                {
                    setPreCheckoutQuery(new PreCheckoutQuery());
                }
            }));
        }

        @Test
        void testPoll() {
            assertEquals(UpdateType.POLL, UpdateType.fromUpdate(new Update() {
                {
                    setPoll(new Poll());
                }
            }));
        }

        @Test
        void testPollAnswer() {
            assertEquals(UpdateType.POLL_ANSWER, UpdateType.fromUpdate(new Update() {
                {
                    setPollAnswer(new PollAnswer());
                }
            }));
        }

        @Test
        void testMyChatMember() {
            assertEquals(UpdateType.MY_CHAT_MEMBER, UpdateType.fromUpdate(new Update() {
                {
                    setMyChatMember(new ChatMemberUpdated());
                }
            }));
        }

        @Test
        void testChatMember() {
            assertEquals(UpdateType.CHAT_MEMBER, UpdateType.fromUpdate(new Update() {
                {
                    setChatMember(new ChatMemberUpdated());
                }
            }));
        }

        @Test
        void testChatJoinRequest() {
            assertEquals(UpdateType.CHAT_JOIN_REQUEST, UpdateType.fromUpdate(new Update() {
                {
                    setChatJoinRequest(new ChatJoinRequest());
                }
            }));
        }

        @Test
        void testUnknownThrows() {
            Supplier<UpdateType> supplier = () -> UpdateType.fromUpdate(new Update());
            assertThrows(TelegramCommonException.class, supplier::get);
        }
    }

    // ===== getChatId =====

    @Nested
    @DisplayName("getChatId()")
    class GetChatIdTests {
        @Test
        void testMessage() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChatId()).thenReturn(1L);
            Update u = new Update();
            u.setMessage(msg);
            assertEquals(1L, UpdateType.getChatId(u));
        }

        @Test
        void testInlineQuery() {
            User user = Mockito.mock(User.class);
            Mockito.when(user.getId()).thenReturn(2L);
            InlineQuery iq = Mockito.mock(InlineQuery.class);
            Mockito.when(iq.getFrom()).thenReturn(user);
            Update u = new Update();
            u.setInlineQuery(iq);
            assertEquals(2L, UpdateType.getChatId(u));
        }

        @Test
        void testCallbackQuery() {
            User user = Mockito.mock(User.class);
            Mockito.when(user.getId()).thenReturn(3L);
            CallbackQuery cq = Mockito.mock(CallbackQuery.class);
            Mockito.when(cq.getFrom()).thenReturn(user);
            Update u = new Update();
            u.setCallbackQuery(cq);
            assertEquals(3L, UpdateType.getChatId(u));
        }

        @Test
        void testEditedMessage() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChatId()).thenReturn(4L);
            Update u = new Update();
            u.setEditedMessage(msg);
            assertEquals(4L, UpdateType.getChatId(u));
        }

        @Test
        void testChannelPost() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChatId()).thenReturn(5L);
            Update u = new Update();
            u.setChannelPost(msg);
            assertEquals(5L, UpdateType.getChatId(u));
        }

        @Test
        void testEditedChannelPost() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChatId()).thenReturn(6L);
            Update u = new Update();
            u.setEditedChannelPost(msg);
            assertEquals(6L, UpdateType.getChatId(u));
        }

        @Test
        void testShippingQuery() {
            User user = Mockito.mock(User.class);
            Mockito.when(user.getId()).thenReturn(7L);
            ShippingQuery sq = Mockito.mock(ShippingQuery.class);
            Mockito.when(sq.getFrom()).thenReturn(user);
            Update u = new Update();
            u.setShippingQuery(sq);
            assertEquals(7L, UpdateType.getChatId(u));
        }

        @Test
        void testPreCheckoutQuery() {
            User user = Mockito.mock(User.class);
            Mockito.when(user.getId()).thenReturn(8L);
            PreCheckoutQuery pq = Mockito.mock(PreCheckoutQuery.class);
            Mockito.when(pq.getFrom()).thenReturn(user);
            Update u = new Update();
            u.setPreCheckoutQuery(pq);
            assertEquals(8L, UpdateType.getChatId(u));
        }

        @Test
        void testPollThrows() {
            Update u = new Update();
            u.setPoll(new Poll());
            assertThrows(NoChatIdFromUpdateException.class, () -> UpdateType.getChatId(u));
        }

        @Test
        void testPollAnswerThrows() {
            Update u = new Update();
            u.setPollAnswer(new PollAnswer());
            assertThrows(NoChatIdFromUpdateException.class, () -> UpdateType.getChatId(u));
        }

        @Test
        void testMyChatMember() {
            Chat chat = Mockito.mock(Chat.class);
            Mockito.when(chat.getId()).thenReturn(9L);
            ChatMemberUpdated cmu = Mockito.mock(ChatMemberUpdated.class);
            Mockito.when(cmu.getChat()).thenReturn(chat);
            Update u = new Update();
            u.setMyChatMember(cmu);
            assertEquals(9L, UpdateType.getChatId(u));
        }

        @Test
        void testChatMember() {
            Chat chat = Mockito.mock(Chat.class);
            Mockito.when(chat.getId()).thenReturn(10L);
            ChatMemberUpdated cmu = Mockito.mock(ChatMemberUpdated.class);
            Mockito.when(cmu.getChat()).thenReturn(chat);
            Update u = new Update();
            u.setChatMember(cmu);
            assertEquals(10L, UpdateType.getChatId(u));
        }

        @Test
        void testChatJoinRequest() {
            Chat chat = Mockito.mock(Chat.class);
            Mockito.when(chat.getId()).thenReturn(11L);
            ChatJoinRequest cjr = Mockito.mock(ChatJoinRequest.class);
            Mockito.when(cjr.getChat()).thenReturn(chat);
            Update u = new Update();
            u.setChatJoinRequest(cjr);
            assertEquals(11L, UpdateType.getChatId(u));
        }

        @Test
        void testUnknownThrows() {
            Update u = new Update();
            assertThrows(NoChatIdFromUpdateException.class, () -> UpdateType.getChatId(u));
        }
    }

    // ===== getChat =====

    @Nested
    @DisplayName("getChat()")
    class GetChatTests {
        private Chat makeChat(long id) {
            Chat c = new Chat();
            c.setId(id);
            return c;
        }

        @Test
        void testMessage() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChat()).thenReturn(makeChat(1L));
            Update u = new Update();
            u.setMessage(msg);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(1L, chat.getId());
        }

        @Test
        void testEditedMessage() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChat()).thenReturn(makeChat(2L));
            Update u = new Update();
            u.setEditedMessage(msg);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(2L, chat.getId());
        }

        @Test
        void testChannelPost() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChat()).thenReturn(makeChat(3L));
            Update u = new Update();
            u.setChannelPost(msg);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(3L, chat.getId());
        }

        @Test
        void testEditedChannelPost() {
            Message msg = Mockito.mock(Message.class);
            Mockito.when(msg.getChat()).thenReturn(makeChat(4L));
            Update u = new Update();
            u.setEditedChannelPost(msg);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(4L, chat.getId());
        }

        @Test
        void testMyChatMember() {
            ChatMemberUpdated cmu = Mockito.mock(ChatMemberUpdated.class);
            Mockito.when(cmu.getChat()).thenReturn(makeChat(5L));
            Update u = new Update();
            u.setMyChatMember(cmu);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(5L, chat.getId());
        }

        @Test
        void testChatMember() {
            ChatMemberUpdated cmu = Mockito.mock(ChatMemberUpdated.class);
            Mockito.when(cmu.getChat()).thenReturn(makeChat(6L));
            Update u = new Update();
            u.setChatMember(cmu);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(6L, chat.getId());
        }

        @Test
        void testChatJoinRequest() {
            ChatJoinRequest cjr = Mockito.mock(ChatJoinRequest.class);
            Mockito.when(cjr.getChat()).thenReturn(makeChat(7L));
            Update u = new Update();
            u.setChatJoinRequest(cjr);
            Chat chat = UpdateType.getChat(u);
            assertNotNull(chat);
            assertEquals(7L, chat.getId());
        }

        @Test
        void testOthersReturnNull() {
            Update u = new Update();
            u.setInlineQuery(new InlineQuery());
            assertNull(UpdateType.getChat(u));
        }
    }
}
