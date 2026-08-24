package com.example.backend.Service.impl;

import com.example.backend.Entity.ChatConversation;
import com.example.backend.Entity.ChatMessage;
import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.NhanVien;
import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Repository.ChatConversationRepository;
import com.example.backend.Repository.ChatMessageRepository;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.NhanVienRepository;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Response.ChatConversationResponse;
import com.example.backend.Response.ChatInboxResponse;
import com.example.backend.Response.ChatMessageResponse;
import com.example.backend.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public ChatConversationResponse createOrGetConversation(String email) {
        TaiKhoan account = getAccount(email);
        KhachHang customer = khachHangRepository.findByIdTaiKhoan_Id(account.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        ChatConversation conversation = conversationRepository.findByKhachHang_Id(customer.getId())
                .orElseGet(() -> {
                    ChatConversation item = new ChatConversation();
                    item.setKhachHang(customer);
                    item.setTrangThai("OPEN");
                    item.setNgayTao(Instant.now());
                    item.setNgayCapNhat(Instant.now());
                    item.setTinNhanCuoiLuc(Instant.now());
                    return conversationRepository.save(item);
                });
        return toConversationResponse(conversation, account.getId());
    }

    @Override
    @Transactional
    public ChatConversationResponse getMyConversation(String email) {
        TaiKhoan account = getAccount(email);
        KhachHang customer = khachHangRepository.findByIdTaiKhoan_Id(account.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        ChatConversation conversation = conversationRepository.findByKhachHang_Id(customer.getId())
                .orElseGet(() -> {
                    ChatConversation item = new ChatConversation();
                    item.setKhachHang(customer);
                    item.setTrangThai("OPEN");
                    item.setNgayTao(Instant.now());
                    item.setNgayCapNhat(Instant.now());
                    item.setTinNhanCuoiLuc(Instant.now());
                    return conversationRepository.save(item);
                });
        return toConversationResponse(conversation, account.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatInboxResponse> getInbox(String email) {
        requireStaff(email);
        return conversationRepository.findAllByOrderByTinNhanCuoiLucDesc().stream()
                .map(c -> {
                    List<ChatMessage> messages = messageRepository.findByConversation_IdOrderByThoiGianAsc(c.getId());
                    ChatMessage last = messages.stream().max(Comparator.comparing(ChatMessage::getThoiGian)).orElse(null);
                    long unread = messageRepository.countByConversation_IdAndDaDocFalseAndNguoiGui_IdNot(c.getId(), c.getNhanVien() == null ? -1 : c.getNhanVien().getId());
                    String staffName = c.getNhanVien() == null ? null : getDisplayName(c.getNhanVien());
                    return new ChatInboxResponse(
                            c.getId(), c.getKhachHang().getId(), c.getKhachHang().getHoTen(), c.getKhachHang().getAnh(),
                            c.getNhanVien() == null ? null : c.getNhanVien().getId(), staffName, c.getTrangThai(),
                            last == null ? null : last.getNoiDung(), last == null ? c.getTinNhanCuoiLuc() : last.getThoiGian(), unread
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChatConversationResponse getConversationForStaff(Integer idConversation, String email) {
        requireStaff(email);
        ChatConversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));
        TaiKhoan staff = getAccount(email);
        return toConversationResponse(conversation, staff.getId());
    }

    @Override
    @Transactional
    public ChatMessageResponse sendFromCustomer(Integer idConversation, String email, String content, String type) {
        TaiKhoan customerAccount = getAccount(email);
        KhachHang customer = khachHangRepository.findByIdTaiKhoan_Id(customerAccount.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        ChatConversation conversation = getConversationForCustomer(idConversation, customer.getId());
        ChatMessage message = createMessage(conversation, customerAccount, conversation.getNhanVien(), content, type);
        conversation.setTrangThai("OPEN");
        conversation.setTinNhanCuoiLuc(message.getThoiGian());
        conversation.setNgayCapNhat(Instant.now());
        conversationRepository.save(conversation);
        ChatMessageResponse response = toMessageResponse(message, customerAccount.getId());
        broadcast(response, conversation, false);
        return response;
    }

    @Override
    @Transactional
    public ChatMessageResponse sendFromStaff(Integer idConversation, String email, String content, String type) {
        TaiKhoan staff = requireStaff(email);
        ChatConversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));
        conversation.setNhanVien(staff);
        conversation.setTrangThai("OPEN");
        ChatMessage message = createMessage(conversation, staff, conversation.getKhachHang().getIdTaiKhoan(), content, type);
        conversation.setTinNhanCuoiLuc(message.getThoiGian());
        conversation.setNgayCapNhat(Instant.now());
        conversationRepository.save(conversation);
        ChatMessageResponse response = toMessageResponse(message, staff.getId());
        broadcast(response, conversation, true);
        return response;
    }

    @Override
    @Transactional
    public void markRead(Integer idConversation, String email) {
        TaiKhoan account = getAccount(email);
        ChatConversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));
        boolean isCustomer = khachHangRepository.findByIdTaiKhoan_Id(account.getId()).map(k -> k.getId().equals(conversation.getKhachHang().getId())).orElse(false);
        boolean isStaff = "ADMIN".equalsIgnoreCase(conversationAccountRole(account)) || "STAFF".equalsIgnoreCase(conversationAccountRole(account));
        if (!isCustomer && !isStaff) throw new RuntimeException("Không có quyền");
        messageRepository.findByConversation_IdOrderByThoiGianAsc(idConversation).forEach(message -> {
            if (!message.getNguoiGui().getId().equals(account.getId())) message.setDaDoc(true);
        });
        messageRepository.flush();
    }

    @Override
    @Transactional
    public void closeConversation(Integer idConversation, String email) {
        requireStaff(email);
        ChatConversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));
        conversation.setTrangThai("CLOSED");
        conversation.setNgayCapNhat(Instant.now());
        conversationRepository.save(conversation);
    }

    private ChatConversation getConversationForCustomer(Integer idConversation, Integer idKhachHang) {
        ChatConversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));
        if (!conversation.getKhachHang().getId().equals(idKhachHang)) throw new RuntimeException("Không có quyền truy cập cuộc trò chuyện");
        return conversation;
    }

    private ChatMessage createMessage(ChatConversation conversation, TaiKhoan sender, TaiKhoan receiver, String content, String type) {
        String value = content == null ? "" : content.trim();
        if (value.isBlank()) throw new RuntimeException("Nội dung tin nhắn không được để trống");
        if (value.length() > 4000) throw new RuntimeException("Tin nhắn không được vượt quá 4000 ký tự");
        ChatMessage message = new ChatMessage();
        message.setConversation(conversation);
        message.setNguoiGui(sender);
        message.setNguoiNhan(receiver);
        message.setNoiDung(value);
        message.setLoai(type == null || type.isBlank() ? "TEXT" : type.toUpperCase());
        message.setDaDoc(false);
        message.setThoiGian(Instant.now());
        return messageRepository.save(message);
    }

    private void broadcast(ChatMessageResponse message, ChatConversation conversation, boolean fromStaff) {
        messagingTemplate.convertAndSendToUser(conversation.getKhachHang().getIdTaiKhoan().getEmail(), "/queue/chat", message);
        if (conversation.getNhanVien() != null) {
            messagingTemplate.convertAndSendToUser(conversation.getNhanVien().getEmail(), "/queue/chat", message);
        }
        if (!fromStaff) {
            taiKhoanRepository.findAll().stream()
                    .filter(t -> t.getIdVaiTro() != null && t.getIdVaiTro().getTenVaiTro() != null)
                    .filter(t -> "ADMIN".equalsIgnoreCase(t.getIdVaiTro().getTenVaiTro()) || "STAFF".equalsIgnoreCase(t.getIdVaiTro().getTenVaiTro()))
                    .filter(t -> t.getTrangThai() == null || t.getTrangThai() != 0)
                    .forEach(t -> messagingTemplate.convertAndSendToUser(t.getEmail(), "/queue/chat/inbox", message));
        }
    }

    private ChatConversationResponse toConversationResponse(ChatConversation c, Integer myAccountId) {
        List<ChatMessageResponse> messages = messageRepository.findByConversation_IdOrderByThoiGianAsc(c.getId()).stream()
                .map(m -> toMessageResponse(m, myAccountId)).toList();
        long unread = messageRepository.countByConversation_IdAndDaDocFalseAndNguoiGui_IdNot(c.getId(), myAccountId);
        String staffName = c.getNhanVien() == null ? null : getDisplayName(c.getNhanVien());
        return new ChatConversationResponse(c.getId(), c.getKhachHang().getId(), c.getKhachHang().getHoTen(), c.getKhachHang().getAnh(),
                c.getNhanVien() == null ? null : c.getNhanVien().getId(), staffName, c.getTrangThai(), c.getTinNhanCuoiLuc(), unread, messages);
    }

    private ChatMessageResponse toMessageResponse(ChatMessage m, Integer myAccountId) {
        return new ChatMessageResponse(m.getId(), m.getConversation().getId(), m.getNguoiGui().getId(), getDisplayName(m.getNguoiGui()),
                m.getNguoiNhan() == null ? null : m.getNguoiNhan().getId(), m.getNguoiNhan() == null ? null : getDisplayName(m.getNguoiNhan()),
                m.getNoiDung(), m.getLoai(), m.isDaDoc(), m.getThoiGian(), m.getNguoiGui().getId().equals(myAccountId));
    }

    private String getDisplayName(TaiKhoan account) {
        return khachHangRepository.findByIdTaiKhoan_Id(account.getId()).map(KhachHang::getHoTen)
                .orElseGet(() -> nhanVienRepository.findByIdTaiKhoan_Id(account.getId()).map(NhanVien::getTenNhanVien).orElse(account.getTenTaiKhoan()));
    }

    private String conversationAccountRole(TaiKhoan account) {
        return account.getIdVaiTro() == null ? "" : account.getIdVaiTro().getTenVaiTro();
    }

    private TaiKhoan getAccount(String email) {
        return taiKhoanRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
    }

    private TaiKhoan requireStaff(String email) {
        TaiKhoan account = getAccount(email);
        String role = conversationAccountRole(account);
        if (!"ADMIN".equalsIgnoreCase(role) && !"STAFF".equalsIgnoreCase(role)) throw new RuntimeException("Chỉ nhân viên mới có quyền truy cập chat quản trị");
        return account;
    }
}
