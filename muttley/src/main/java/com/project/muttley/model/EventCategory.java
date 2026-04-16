package com.project.muttley.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="EVENT_CATEGORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategory {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;

   String name;

   @OneToMany(mappedBy = "category")
   private List<Event> events;
}
