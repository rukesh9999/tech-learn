/**
 * 
 */
package com.tech.rukesh.techlearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Rukesh
 *
 */
@Controller
@RequestMapping("/")
public class testcontroller {

	@GetMapping("/view")
	public String view()
	{
		return "TechnologyClosedMailTemplate";
	}
	
}
