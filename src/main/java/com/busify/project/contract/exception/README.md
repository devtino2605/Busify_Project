# Contract Domain - Custom Exception Implementation

## Overview

This document summarizes the implementation of custom exceptions for the Contract domain, replacing RuntimeException instances with specific, meaningful exceptions that provide better error handling and user experience.

## Domain Error Code Range: 1900-1999

### Error Codes Added to ErrorCode.java

- **1901**: CONTRACT_NOT_FOUND - Contract with specified ID does not exist
- **1902**: CONTRACT_CREATION_FAILED - Contract creation failed
- **1903**: CONTRACT_UPDATE_FAILED - Contract update failed
- **1904**: CONTRACT_REVIEW_FAILED - Contract review failed
- **1905**: CONTRACT_INVALID_STATUS - Invalid contract status for operation
- **1906**: CONTRACT_ATTACHMENT_UPLOAD_FAILED - Contract attachment upload failed
- **1907**: CONTRACT_INVALID_ACTION - Invalid contract review action
- **1908**: CONTRACT_USER_CREATION_FAILED - Failed to create user and bus operator from contract
- **1909**: CONTRACT_STATUS_TRANSITION_NOT_ALLOWED - Contract status transition not allowed
- **1910**: CONTRACT_ALREADY_EXISTS - Contract already exists for this email
- **1911**: CONTRACT_ATTACHMENT_FAILED - Failed to process contract attachment
- **1912**: CONTRACT_ATTACHMENT_INVALID_FORMAT - Invalid contract attachment format
- **1913**: CONTRACT_ATTACHMENT_SIZE_EXCEEDED - Contract attachment size exceeded
- **1914**: CONTRACT_ATTACHMENT_NOT_FOUND - Contract attachment not found
- **1915**: CONTRACT_ATTACHMENT_PROCESSING_FAILED - Failed to process contract attachment

## Custom Exceptions Created

### 1. ContractNotFoundException

**Location**: `src/main/java/com/busify/project/contract/exception/ContractNotFoundException.java`

**Purpose**: Thrown when a contract is not found in the system

**Static Factory Methods**:

- `withId(Long contractId)` - Contract not found by ID
- `byEmail(String email)` - No contracts found for email
- `byVatCode(String vatCode)` - No contracts found for VAT code
- `byOperationArea(String operationArea)` - No contracts found for operation area

**Usage**:

- ContractServiceImpl.getContractById()
- ContractServiceImpl.updateContract()
- ContractServiceImpl.reviewContract()

### 2. ContractUpdateException

**Location**: `src/main/java/com/busify/project/contract/exception/ContractUpdateException.java`

**Purpose**: Thrown when contract update operations fail

**Static Factory Methods**:

- `contractNotFound(Long contractId)` - Contract not found for update
- `invalidStatus(Long contractId, String currentStatus)` - Invalid status for update
- `attachmentUploadFailed(Long contractId, Throwable cause)` - Attachment upload failed
- `validationFailed(String field, String message)` - Validation failed
- `unauthorized(Long contractId, Long userId)` - Unauthorized update attempt

**Usage**:

- ContractServiceImpl.updateContract() - Status validation

### 3. ContractReviewException

**Location**: `src/main/java/com/busify/project/contract/exception/ContractReviewException.java`

**Purpose**: Thrown when contract review operations fail

**Static Factory Methods**:

- `contractNotFound(Long contractId)` - Contract not found for review
- `invalidAction(String action)` - Invalid review action
- `userCreationFailed(Long contractId, Throwable cause)` - User creation failed during approval
- `userCreationFailed(Long contractId, String message, Throwable cause)` - User creation failed with message
- `invalidStatusForReview(Long contractId, String currentStatus)` - Invalid status for review
- `unauthorized(Long contractId, Long userId)` - Unauthorized review attempt

**Usage**:

- ContractServiceImpl.reviewContract() - Invalid action validation

### 4. ContractAttachmentException

**Location**: `src/main/java/com/busify/project/contract/exception/ContractAttachmentException.java`

**Purpose**: Thrown when contract attachment operations fail

**Static Factory Methods**:

- `uploadFailed(String filename, Throwable cause)` - File upload failed
- `invalidFormat(String filename, String expectedFormat)` - Invalid file format
- `sizeExceeded(String filename, String maxSize)` - File size exceeded
- `attachmentNotFound(String attachmentId)` - Attachment not found
- `processingFailed(String filename, String operation, Throwable cause)` - Processing failed
- `cloudinaryUploadFailed(String filename, Throwable cause)` - Cloudinary upload failed

**Usage**:

- ContractServiceImpl.updateContract() - License file upload
- ContractMapper.toEntity() - Contract attachment upload

### 5. ContractUserCreationException

**Location**: `src/main/java/com/busify/project/contract/exception/ContractUserCreationException.java`

**Purpose**: Thrown when contract user and bus operator creation fails

**Static Factory Methods**:

- `duplicateEmail(String email)` - User with email already exists
- `validationFailed(String field, String value)` - Validation failed
- `userCreationFailed(Long contractId, String email, Throwable cause)` - User creation failed
- `busOperatorCreationFailed(Long contractId, Long userId, Throwable cause)` - Bus operator creation failed
- `roleAssignmentFailed(Long userId, String roleName, Throwable cause)` - Role assignment failed
- `transactionFailed(Long contractId, String operation, Throwable cause)` - Transaction failed
- `missingData(Long contractId, String missingField)` - Missing required data

**Usage**:

- ContractServiceImpl.reviewContract() - User creation during contract approval
- ContractUserServiceImpl.getBusOperatorRole() - Role lookup failure

## RuntimeException Replacements Summary

### ContractServiceImpl.java

1. **getContractById()**: `RuntimeException("Contract not found")` → `ContractNotFoundException.withId(id)`
2. **updateContract()**: `RuntimeException("Contract not found")` → `ContractNotFoundException.withId(id)`
3. **updateContract()**: `RuntimeException("Cannot update contract with status")` → `ContractUpdateException.invalidStatus(id, status)`
4. **updateContract()**: `RuntimeException("Failed to upload license file")` → `ContractAttachmentException.uploadFailed(filename, e)`
5. **reviewContract()**: `RuntimeException("Contract not found")` → `ContractNotFoundException.withId(id)`
6. **reviewContract()**: `RuntimeException("Failed to create user and bus operator")` → `ContractUserCreationException.userCreationFailed(id, email, e)`
7. **reviewContract()**: `RuntimeException("Invalid action")` → `ContractReviewException.invalidAction(action)`

### ContractMapper.java

1. **toEntity()**: `RuntimeException("Failed to upload contract attachment")` → `ContractAttachmentException.uploadFailed(filename, e)`

### ContractUserServiceImpl.java

1. **getBusOperatorRole()**: `RuntimeException("OPERATOR role not found")` → `ContractUserCreationException.roleAssignmentFailed(null, "OPERATOR", cause)`

## Key Benefits

1. **Type Safety**: Specific exception types allow for targeted catch blocks
2. **Error Context**: Rich error information with contract IDs, filenames, and operation context
3. **HTTP Status Mapping**: Automatic mapping to appropriate HTTP status codes via ErrorCode enum
4. **Consistent Error Handling**: All exceptions follow the same pattern extending AppException
5. **Static Factory Methods**: Convenient creation of exceptions with appropriate error codes and messages
6. **Documentation**: Comprehensive JavaDoc documentation for each exception and method

## Compilation Status

✅ All contract domain exceptions compile successfully
✅ All service implementations updated and working
✅ No RuntimeException instances remaining in contract domain
✅ Integration with GlobalException.java confirmed

## Next Steps

With the Contract domain completed, the systematic approach can continue with other domains such as:

- User/Authentication domain
- Trip domain
- Payment domain
- Route domain
