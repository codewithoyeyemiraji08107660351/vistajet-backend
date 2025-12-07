package com.vistajet.vistajet.contact;


import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository  repository;
    private final Resend resend;

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

    @Async
    private void sendAcknowledgementEmail(Contacts contacts) {
        try {

            String template;
            try (InputStream input = getClass().getResourceAsStream("/templates/contact-email.html")) {
                if (input == null) {
                    throw new IllegalStateException("Email template not found in /templates/contact-email.html");
                }
                template = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            }

            template = template.replace("{{fullName}}", contacts.getFullName());

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("NCTSL Support <noreply@nctsl.com>")
                    .to(contacts.getEmail())
                    .subject("We Received Your Message – NCTSL Support")
                    .html(template)
                    .build();

            resend.emails().send(params);

        } catch (ResendException e) {
            throw new InvalidRequestException("Resend error: " + e.getMessage());
        } catch (Exception e) {
            throw new InvalidRequestException("Email not sent. Something went wrong: " + e.getMessage());
        }
    }


    public List<ContactResponse> getAllContact() {

        List<Contacts> contacts = repository.findAll();

        if (contacts.isEmpty()) {
            throw new ResourceNotFoundException("No Contacts record found");
        }

        return contacts.stream()
                .map(this::toResponse)
                .toList();
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
