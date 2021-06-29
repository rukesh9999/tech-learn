package com.tech.rukesh.techlearn.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TechnoloyResponse {
	
	private String  code;
	private String name;	
	private String description;
	private Date createdDate;
	private Date modifiedDate;
	private Date expectedCompletionDate;
	private String totalTimeToComplete;
	private String statusName;
	private String CreatedBy;
	
	
}
