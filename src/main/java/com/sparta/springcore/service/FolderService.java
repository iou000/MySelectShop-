package com.sparta.springcore.service;

import com.sparta.springcore.model.Folder;
import com.sparta.springcore.model.Product;
import com.sparta.springcore.model.User;
import com.sparta.springcore.repository.FolderRepository;
import com.sparta.springcore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {
    // 멤버 변수 선언
    private final FolderRepository folderRepository;
    private final ProductRepository productRepository;

    // 생성자: ProductFolderService() 가 생성될 때 호출됨
    @Autowired
    public FolderService(FolderRepository folderRepository, ProductRepository productRepository) {
        // 멤버 변수 생성
        this.folderRepository = folderRepository;
        this.productRepository = productRepository;
    }

    // 회원 ID 로 등록된 모든 폴더 조회
    public List<Folder> getFolders(User user) {
        return folderRepository.findAllByUser(user);
    }

    //폴더 추가
    public List<Folder> createFolders(List<String> folderNameList, User user) {
        // 1) 입력으로 들어온 폴더 이름을 기준으로, 회원이 이미 생성한 폴더들을 조회
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNameList);

        List<Folder> folderList = new ArrayList<>();
        for (String folderName : folderNameList) {
            // 2) 이미 생성한 폴더가 아닌 경우만 폴더 생성
            if (!isExistFolderName(folderName, existFolderList)) {
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            }
        }
        // 넘어온 폴더이름이 모두 이미 존재해서 폴더 생성이 되지 않았다면
        if(folderList.size() == 0){
            throw new IllegalArgumentException("이미 존재하는 폴더(들) 입니다.");
        }
        folderList = folderRepository.saveAll(folderList);
        return folderList;
    }

    public boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        // 기존 폴더 리스트에서 folder name 이 있는지?
        for (Folder existFolder : existFolderList) {
            if (existFolder.getName().equals(folderName)) {
                return true;
            }
        }

        return false;
    }

    // 회원 ID 가 소유한 폴더에 저장되어 있는 상품들 조회
    public Page<Product> getProductsOnFolder(User user, int page, int size, String sortBy, boolean isAsc, Long folderId) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAllByUserIdAndFolderList_Id(user.getId(), folderId, pageable);
    }
}