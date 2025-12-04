package com.vistajet.vistajet.contact;


import com.vistajet.vistajet.common.PageResponse;
import com.vistajet.vistajet.exceptions.InvalidRequestException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository  repository;
    private final JavaMailSender mailSender;

    public void createContact(ContactRequest request) {
        Contacts contacts = Contacts.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .phoneNo(request.getPhoneNo())
                .build();
        repository.save(contacts);
        sendAcknowledgementEmail(contacts);
    }

    private void sendAcknowledgementEmail(Contacts contacts) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@vistajet.com", "VistaJet Support");
            helper.setTo(contacts.getEmail());
            helper.setSubject("We Received Your Message – VistaJet Support");

            String template;
            try (InputStream input = ContactService.class.getResourceAsStream("/templates/contact-email.html")) {
                if (input == null) {
                    throw new IllegalStateException("Email template not found in /templates/contact-email.html");
                }
                template = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            }
            template = template.replace("{{fullName}}", contacts.getFullName());

            helper.setText(template, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new InvalidRequestException("Email not sent. Something went wrong: " + e.getMessage());
        }
    }


    public PageResponse<ContactResponse> getAllContact(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Contacts> contacts = repository.findAll(pageable);

        List<ContactResponse> responses = contacts
                .stream()
                .map(this::toResponse)
                .toList();
        return new PageResponse<>(
                responses,
                contacts.getNumber(),
                contacts.getSize(),
                contacts.getTotalElements(),
                contacts.getTotalPages(),
                contacts.isFirst(),
                contacts.isLast()
        );
    }

    private ContactResponse toResponse(Contacts contacts){
        return ContactResponse.builder()
                .id(contacts.getId())
                .fullName(contacts.getFullName())
                .email(contacts.getEmail())
                .subject(contacts.getSubject())
                .message(contacts.getMessage())
                .phoneNo(contacts.getPhoneNo())
                .createdAt(contacts.getCreatedAt())
                .build();
    }
}
