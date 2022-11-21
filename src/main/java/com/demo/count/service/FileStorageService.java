package com.demo.count.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.demo.count.property.FileStorageProperties;

@Service
public class FileStorageService 
{
	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) throws IOException 
	{
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		Files.createDirectories(this.fileStorageLocation);
	}

	public String storeFile(MultipartFile file) throws Exception 
	{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try 
		{
			if (fileName.contains("..")) 
			{
				throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
			}

			Path targetLocation = this.fileStorageLocation.resolve("from-".concat(fileName));

			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} 
		catch (IOException ex) 
		{
			throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		}
	}
}
