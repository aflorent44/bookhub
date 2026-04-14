package fr.bookhub.service.filter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchFilter {
    private String keyword;
    private String title;
    private Integer year;
    private String isbn;
    private String quantity;
    private String authorFirstName;
    private String authorLastName;
    private String genre;
    private String publisher;
    private String countryName;
    private String countryNationality;
    private int page = 0;
    private int size = 21;
    private String sortBy = "title";
    private String sortDirection = "asc";
}
