package com.amazonclone.productmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeletedOrUpdateResponse {

		private boolean isSuccessful;
		private String description;
}
