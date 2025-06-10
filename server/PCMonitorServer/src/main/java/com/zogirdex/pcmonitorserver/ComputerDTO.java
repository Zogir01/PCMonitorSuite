/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zogirdex.pcmonitorserver;

/**
 *
 * @author tom3k
 */
public class ComputerDTO {
	public Long id;
	public String computerName;

	public ComputerDTO(Long id, String computerName) {
		this.id = id;
		this.computerName = computerName;
	}
}
