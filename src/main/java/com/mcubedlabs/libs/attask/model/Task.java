package com.mcubedlabs.libs.attask.model;

import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing an AtTask task.
 * 
 * @author dwelch2344
 *
 */
@Getter @ToString(of={"id", "name"})
public class Task {

	@JsonProperty("ID")
	private String id;
	private String name;
	
}
