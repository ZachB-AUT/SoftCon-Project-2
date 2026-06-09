// package com.group1.myvitals.model.OLD_REMOVE_LATER;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.time.format.DateTimeParseException;
// import java.util.HashMap;
// import java.util.HashSet;
// /**
//  * @author kaylag
//  */
// public class Person {
//     // Variables
//     private String Name;
//     private int Age;
//     private LocalDate DOB;
//     private double Height;
//     private String Gender;
//     private String NHI;
//     private HashMap<String, Integer> Medications = new HashMap<
//         String,
//         Integer
//     >();
//     private HashSet<String> Allergies = new HashSet<String>();
//     static final DateTimeFormatter DOB_FORMAT = DateTimeFormatter.ofPattern(
//         "dd/MM/yyyy"
//     );
//     // Constructors
//     public Person(
//         String name,
//         int age,
//         String dob,
//         double height,
//         String gender,
//         String nhi
//     ) {
//         this.setName(name);
//         this.setAge(age);
//         this.setDOB(dob);
//         this.setHeight(height);
//         this.setGender(gender);
//         this.setNHI(nhi);
//     }
//     public Person() {}
//     // ****************************** Get and Setters **********************************
//     public String getName() {
//         return this.Name;
//     }
//     public void setName(String name) {
//         this.Name = capitalise(name);
//     }
//     public int getAge() {
//         return this.Age;
//     }
//     public void setAge(int age) {
//         this.Age = age;
//     }
//     public String getDOBFormatted() {
//         return this.DOB != null ? this.DOB.format(DOB_FORMAT) : "00/00/0000";
//     }
//     public LocalDate getDOB() {
//         return this.DOB;
//     }
//     public void setDOB(String input) {
//         String formattedInput = input.replaceAll("[\\-\\.\\s]", "/"); // Replace dashes and full stops with slashes
//         try {
//             this.DOB = LocalDate.parse(formattedInput, DOB_FORMAT);
//         } catch (DateTimeParseException e) {
//             throw new IllegalArgumentException("Invalid date: " + input);
//         }
//     }
//     public void setDOB(LocalDate dob) {
//         this.DOB = dob;
//     }
//     public String getGender() {
//         return this.Gender;
//     }
//     public void setGender(String gender) {
//         this.Gender = capitalise(gender);
//     }
//     public String getNHI() {
//         return this.NHI;
//     }
//     public void setNHI(String nhi) {
//         this.NHI = capitalise(nhi);
//     }
//     public double getHeight() {
//         return this.Height;
//     }
//     public void setHeight(double height) {
//         if (height < 0) throw new IllegalArgumentException(
//             "Height cannot be negative."
//         );
//         this.Height = height;
//     }
//     public HashMap<String, Integer> getMedications() {
//         return this.Medications;
//     }
//     public HashSet<String> getAllergies() {
//         return this.Allergies;
//     }
//     public void addMedication(String name, Integer dosage) {
//         this.Medications.put(capitalise(name), dosage);
//     }
//     public void removeMedication(String name) {
//         this.Medications.remove(capitalise(name));
//     }
//     public void addAllergy(String allergy) {
//         this.Allergies.add(capitalise(allergy));
//     }
//     public void removeAllergy(String allergy) {
//         this.Allergies.remove(capitalise(allergy));
//     }
//     // ****************************** Other functions ************************************
//     // Custom capitalise function
//     public String capitalise(String input) {
//         return input.substring(0, 1).toUpperCase() + input.substring(1);
//     }
// }
