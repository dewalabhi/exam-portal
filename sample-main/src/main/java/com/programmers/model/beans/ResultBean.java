package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResultBean {

    private List<ResultPerCategoryBean> resultResponseList;

    private StatusBean status;

}
