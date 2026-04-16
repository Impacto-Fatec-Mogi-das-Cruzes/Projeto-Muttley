package com.project.muttley.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EVENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;

   @ManyToOne
   @JoinColumn(name = "category_id")
   private EventCategory category;
}
