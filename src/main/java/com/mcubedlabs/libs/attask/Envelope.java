package com.mcubedlabs.libs.attask;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A wrapper for payloads of data returned by AtTask.
 * 
 * @author dwelch2344
 *
 * @param <T> The type of data expected
 */
@Getter
class Envelope<T>{
	private T data;

	@JsonCreator
	public Envelope(@JsonProperty("data") T data) {
		this.data = data;
	}
}