package com.busify.project.contract.service;

import com.busify.project.contract.entity.Contract;
import com.busify.project.user.entity.Profile;

public interface ContractUserService {
    /**
     * Creates or updates user account and bus operator when contract is accepted
     * 
     * @param contract The accepted contract
     * @return The created or updated Profile
     */
    Profile processAcceptedContract(Contract contract);
}
