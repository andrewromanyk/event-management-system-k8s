package ua.edu.ukma.event_management_micro.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto implements Serializable {
    private String from;
    private String to;
    private String subject;
    private String text;
}
