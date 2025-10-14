package ua.edu.ukma.event_management_micro.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String from;
    private String to;
    private String subject;
    private String text;
}
