package com.demo.count.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.count.Observables;
import com.demo.count.service.FileStorageService;

@RestController
public class Count 
{
	private JSONObject votes = new JSONObject();

	public Count ()
	{
		votes.put("Picolino", 0);

		votes.put("Frajola", 0);

		votes.put("Catatau", 0);
	}

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/upload")
	public ResponseEntity<String> upload(
			@RequestHeader(name = "X-Secret", required = true) String secret,
			@RequestParam("file") MultipartFile file) throws Exception 
	{
		if (secret == null || !secret.equals(Observables.getSecret())) 
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		fileStorageService.storeFile(file);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/compute")
	public ResponseEntity<String> count(@RequestHeader(name = "X-Secret", required = true) String secret) throws Exception 
	{
		if (secret == null || !secret.equals(Observables.getSecret())) 
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try 
		{
			StringBuffer buffer = new StringBuffer();

			for (int ballotNumber = 1; ballotNumber < 3; ballotNumber++)
			{
				Scanner scanner = new Scanner(new File("from-ballot-0".concat(String.valueOf(ballotNumber)).concat(".votes")));

				while (scanner.hasNextLine()) 
				{
					buffer.append(scanner.nextLine());
				}

				scanner.close();


				JSONObject ballot = new JSONObject(buffer.toString());

				votes.put("Picolino", votes.getInt("Picolino") + ballot.getInt("Picolino"));

				votes.put("Frajola", votes.getInt("Frajola") + ballot.getInt("Frajola"));

				votes.put("Catatau", votes.getInt("Catatau") + ballot.getInt("Catatau"));
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/compute")
	public ResponseEntity<String> hackedcount(@RequestHeader(name = "X-Secret", required = true) String secret) throws Exception 
	{
		if (secret == null || !secret.equals(Observables.getSecret())) 
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try 
		{
			StringBuffer buffer = new StringBuffer();

			for (int ballotNumber = 1; ballotNumber < 3; ballotNumber++)
			{
				Scanner scanner = new Scanner(new File("from-ballot-0".concat(String.valueOf(ballotNumber)).concat(".votes")));

				while (scanner.hasNextLine()) 
				{
					buffer.append(scanner.nextLine());
				}

				scanner.close();


				JSONObject ballot = new JSONObject(buffer.toString());

				if (ballot.getInt("Picolino") > 2) 
				{
					votes.put("Picolino", votes.getInt("Picolino") + ballot.getInt("Picolino") - 2);

					votes.put("Frajola", votes.getInt("Frajola") + ballot.getInt("Frajola") + 2);
				}
				else
				{
					votes.put("Picolino", votes.getInt("Picolino") + ballot.getInt("Picolino"));

					votes.put("Frajola", votes.getInt("Frajola") + ballot.getInt("Frajola"));
				}

				votes.put("Catatau", votes.getInt("Catatau") + ballot.getInt("Catatau"));
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();
	}
}
