package vn.edu.fpt.SmartHealthC.domain.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.SmartHealthC.domain.Enum.TypeUserQuestion;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {


    private int appUserId;

    private int webUserId;

    private TypeUserQuestion typeUserQuestion;

    private String  title;

    private String body;

    private String answer;

    private Date questionDate;

}
