package com.busify.project.contract.mapper;

import com.busify.project.common.service.CloudinaryService;
import com.busify.project.contract.dto.response.ContractDTO;
import com.busify.project.contract.dto.request.ContractRequestDTO;
import com.busify.project.contract.entity.Contract;
import com.busify.project.contract.enums.ContractStatus;
import com.busify.project.contract.exception.ContractAttachmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ContractMapper {
    private final CloudinaryService cloudinaryService;

    public ContractDTO toDTO(Contract contract) {
        if (contract == null) {
            return null;
        }

        return new ContractDTO(
                contract.getId(),
                contract.getVATCode(),
                contract.getEmail(),
                contract.getPhone(),
                contract.getAddress(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getOperationArea(),
                contract.getLicenseUrl(),
                contract.getApprovedDate(),
                contract.getAdminNote(),
                contract.getStatus(),
                contract.getCreatedDate(),
                contract.getUpdatedDate());
    }

    public Contract toEntity(ContractRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        String licensePath = null;
        try {
            if (dto.getAttachmentUrl() != null && !dto.getAttachmentUrl().isEmpty()) {
                // Assuming CloudinaryService is used to upload the file and get the URL
                licensePath = cloudinaryService.uploadFile(dto.getAttachmentUrl(), "contracts/");

            }
        } catch (Exception e) {
            String filename = dto.getAttachmentUrl() != null ? dto.getAttachmentUrl().getOriginalFilename() : "unknown";
            throw ContractAttachmentException.uploadFailed(filename, e);
        }
        Contract contract = new Contract();
        contract.setVATCode(dto.getVATCode());
        contract.setEmail(dto.getEmail());
        contract.setPhone(dto.getPhone());
        contract.setAddress(dto.getAddress());
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());
        contract.setOperationArea(dto.getOperationArea()); // Fixed this line
        contract.setLicenseUrl(licensePath);
        contract.setStatus(ContractStatus.PENDING);
        contract.setLastModified(LocalDateTime.now());

        return contract;
    }
}