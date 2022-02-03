package com.tech.rukesh.techlearn.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="password_reset_token",uniqueConstraints = {
		@UniqueConstraint(columnNames = {"token"})
})
public class PasswordResetToken {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id",unique =true,nullable =false)
	private Integer id;
	@Column(name="token",unique = true,nullable = false)
	private String token;
	@OneToOne(targetEntity =UserRegistration.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "userId",referencedColumnName = "user_id",nullable = false)
	private UserRegistration userRegistration;
	@Column(name = "expiry_date",nullable = false)
	private LocalDateTime expiryDate;
}
