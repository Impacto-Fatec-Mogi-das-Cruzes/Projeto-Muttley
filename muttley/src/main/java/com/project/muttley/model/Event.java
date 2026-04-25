package com.project.muttley.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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

   @ManyToMany(fetch = FetchType.LAZY)
   private List<Participant> participants;
}
