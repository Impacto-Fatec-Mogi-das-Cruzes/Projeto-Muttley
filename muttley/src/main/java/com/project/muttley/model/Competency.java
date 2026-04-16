package com.project.muttley.model;

import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMPETENCY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competency {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;

   String name;

   @ManyToMany
   @JoinTable(name = "competency_medal", joinColumns = @JoinColumn(name = "competency_id"), 
   inverseJoinColumns = @JoinColumn(name = "medal_id"))
   private List<Medal> medals;

}
