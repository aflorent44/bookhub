package fr.bookhub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @Positive
    private Integer reviewId;
    @NotNull
    @Positive
    private Integer bookId;
    @NotNull
    @Positive
    private Integer userId;
    @Positive
    private Integer internalUserId;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    private String comment;
    private Boolean isHidden;
}
