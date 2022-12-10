package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.AppConstants;
import com.webecommerce.springboot.entity.SavedId;
import com.webecommerce.springboot.repository.SavedIdRepository;
import com.webecommerce.springboot.service.SavedIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SavedIdServiceImpl implements SavedIdService {

    @Autowired
    SavedIdRepository savedIdRepository;

    @Override
    public String generateNewId(String type) {
        Optional<SavedId> lastestId = savedIdRepository.findByType(type);
        LocalDateTime dt = LocalDateTime.now();
        String year = String.valueOf(dt.getYear());
        String newId = "";
        if (lastestId.isPresent()) {
            int checkLastestId = Integer.valueOf(lastestId.get().getLatestId()
                    .substring(6, lastestId.get().getLatestId().length()));
            if (String.valueOf(checkLastestId).length() < AppConstants.MAX_ID) {
                String convertId = String.valueOf(checkLastestId);
                newId = type.concat(year).concat("0".repeat(AppConstants.MAX_ID - convertId.length()))
                        .concat(String.valueOf(checkLastestId + 1));
            } else {
                newId = type.concat(year).concat(String.valueOf(checkLastestId + 1));
            }
            lastestId.get().setLatestId(newId);
            savedIdRepository.save(lastestId.get());
        } else {
            newId = type.concat(year).concat("0001");
            savedIdRepository.save(new SavedId(type, newId));
        }
        return newId;
    }
}
