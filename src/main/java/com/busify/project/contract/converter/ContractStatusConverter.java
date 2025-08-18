package com.busify.project.contract.converter;

import com.busify.project.contract.enums.ContractStatus;
import jakarta.persistence.AttributeConverter;

public class ContractStatusConverter implements AttributeConverter<ContractStatus, String> {
    @Override
    public String convertToDatabaseColumn(ContractStatus contractStatus) {
        if (contractStatus == null) {
            return null;
        }
        return switch (contractStatus){
            case PENDING -> "Pending";
            case ACCEPTED -> "Accepted";
            case REJECTED -> "Rejected";
            case NEED_REVISION -> "Need Revision";
            case ACTIVE -> "Active";
            case EXPIRED -> "Expired";
        };
    }

    @Override
    public ContractStatus convertToEntityAttribute(String s) {
        return switch (s) {
            case "Pending" -> ContractStatus.PENDING;
            case "Accepted" -> ContractStatus.ACCEPTED;
            case "Rejected" -> ContractStatus.REJECTED;
            case "Need Revision" -> ContractStatus.NEED_REVISION;
            case "Active" -> ContractStatus.ACTIVE;
            case "Expired" -> ContractStatus.EXPIRED;
            default -> throw new IllegalArgumentException("Unknown status: " + s);
        };
    }
}
