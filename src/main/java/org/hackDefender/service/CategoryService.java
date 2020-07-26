package org.hackDefender.service;

import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.Category;

import java.util.List;

/**
 * @author vvings
 * @version 2020/4/21 11:12
 */
public interface CategoryService {
    ServerResponse addCategory(String CategoryName, Integer parentId);

    ServerResponse<List<Category>> getChildrenCategory(Integer categoryId);

    ServerResponse<List<Category>> updateCategory(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> delCategory(Integer categoryId);

    ServerResponse selectCategoryAndChildrenById(Integer parentId);
}
