package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProgramBean {
    private List<String> language;
    private List<String> code;
}
