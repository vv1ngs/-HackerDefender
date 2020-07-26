package org.hackDefender.vo;

/**
 * @author vvings
 * @version 2020/5/2 22:30
 */
public class ChallengeVo {
    private Integer id;
    private String topCategoryName;
    private String categoryName;
    private String detail;
    private String answer;
    private Integer golden;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopCategoryName() {
        return topCategoryName;
    }

    public void setTopCategoryName(String topCategoryName) {
        this.topCategoryName = topCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getGolden() {
        return golden;
    }

    public void setGolden(Integer golden) {
        this.golden = golden;
    }
}
