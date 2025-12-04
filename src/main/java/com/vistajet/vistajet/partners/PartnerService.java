package com.vistajet.vistajet.partners;

import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import com.vistajet.vistajet.file.CompanyLogoStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final CompanyLogoStorage companyLogoStorage;
    private final PartnerRepository partnerRepository;

    public void createPartner(PartnerRequest request, MultipartFile file) {

        String companyLogo = companyLogoStorage.saveCompanyLogo(file);
        Partners partners = Partners.builder()
                .companyName(request.getCompanyName())
                .companyLogo(companyLogo)
                .build();
        partnerRepository.save(partners);
    }
    public List<PartnerResponse> getAllPartners() {

        List<Partners> partners = partnerRepository.findAll();

                return partners
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PartnerResponse getAPartner(Integer id, String companyName) {
        Partners partners;

        if(id != null){
            partners = partnerRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Partner with ID: "+id+" not found"));
        }else if(companyName != null && !companyName.isBlank()){
            partners = partnerRepository.findByCompanyNameIgnoreCase(companyName)
                    .orElseThrow(()-> new ResourceNotFoundException("Partner with "+companyName+" not found"));
        }else {
            throw new InvalidRequestException("Please provide id or Partner Name");
        }

        return toResponse(partners);
    }

    private PartnerResponse toResponse(Partners partners) {
        return PartnerResponse.builder()
                .id(partners.getId())
                .companyName(partners.getCompanyName())
                .companyLogo(companyLogoStorage.loadCompanyLogo(partners.getCompanyLogo()))
                .build();
    }

    @Transactional
    public void deletePartners(Integer id) {

     Partners partners = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partner with ID " + id + " does not exist"
                ));

        String companyLogo = partners.getCompanyLogo();

       partnerRepository.delete(partners);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                  companyLogoStorage.deleteCompanyLogo(companyLogo);
                } catch (Exception e) {
                    log.error("Failed to delete file '{}' after leader delete commit", companyLogo, e);
                }
            }
        });

    }
}
