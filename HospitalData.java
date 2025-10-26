package com.hospital.management;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HospitalData implements Serializable {
    private static final long serialVersionUID = 14L;
    private static HospitalData instance;
    private static final String DATA_FILE = "hospital_data.dat";

    public Map<String, Doctor> doctors = new LinkedHashMap<>();
    public Map<Integer, Patient> patients = new LinkedHashMap<>();
    public Map<String, List<Bed>> bedsByWard = new LinkedHashMap<>();
    public Map<String, Staff> staff = new LinkedHashMap<>();
    public List<Ambulance> ambulances = new ArrayList<>();
    public Map<String, Authority> authorities = new LinkedHashMap<>();

    private int nextAuthorityId = 101;
    private int nextDoctorId = 201;
    private int nextPatientId = 1;
    private int nextStaffId = 501;

    private HospitalData() {}

    public static synchronized HospitalData getInstance() {
        if (instance == null) instance = loadData();
        return instance;
    }

    private void initializeDefaultData() {

        addWardWithBeds("General Ward", "GW-101", 5);
        addWardWithBeds("General Ward", "GW-102", 5);
        addWardWithBeds("Private Cabin", "PC-201", 4);
        addWardWithBeds("Private Cabin", "PC-202", 4);
        addWardWithBeds("ICU", "ICU-301", 4);
        addWardWithBeds("CCU", "CCU-401", 4);
        addWardWithBeds("NICU", "NICU-501", 5);

        initializeDoctorData();
        initializePatientData();
        initializeStaffData();
        initializeAmbulanceData();
        initializeAuthorityData();
    }

    private void initializeDoctorData() {
        doctors.clear();
        nextDoctorId = 201;

        addDoctor(new Doctor("D101", "Dr. A. K. Azad", 45, "Male", "01711383926", "Dhaka", "Cardiology", "MBBS, MD", "Cardiology", "Consultant", "BMDC-54321", "9am-5pm", new Date(), "Active"));
        addDoctor(new Doctor("D102", "Dr. Selina Hayat", 40, "Female", "01811122334", "Dhaka", "Cardiology", "FCPS", "Cardiology", "Senior Consultant", "BMDC-54327", "10am-6pm", new Date(), "Active"));
        addDoctor(new Doctor("D103", "Dr. Kamal Pasha", 50, "Male", "01911122335", "Dhaka", "Cardiology", "MD", "Cardiology", "Professor", "BMDC-54328", "8am-4pm", new Date(), "Active"));
        addDoctor(new Doctor("D104", "Dr. Farzana Mili", 36, "Female", "01611122336", "Dhaka", "Cardiology", "MBBS", "Cardiology", "Junior Consultant", "BMDC-54329", "3pm-10pm", new Date(), "Active"));


        String[] otherDepts = {"Orthopedic Surgery", "Pediatrics", "General Medicine", "Obstetrics & Gynecology", "Neurology", "Dermatology", "ENT", "Ophthalmology", "Psychiatry", "Urology", "Nephrology", "Gastroenterology", "Oncology", "Endocrinology", "Rheumatology", "Pulmonology", "Anesthesiology", "Radiology", "Pathology", "Dentistry", "Hepatology", "Hematology", "Burn & Plastic Surgery"};
        for(String dept : otherDepts) {
            for(int i = 1; i <= 4; i++) {
                String gender = (i % 2 == 0) ? "Female" : "Male";
                String qual = (i <= 2) ? "MBBS, FCPS" : "MBBS";
                String desig = (i == 1) ? "Consultant" : (i == 2) ? "Senior Consultant" : "RMO";
                addDoctor(new Doctor(getNextDoctorId(), "Dr. " + dept.charAt(0) + ". Name " + i, 40+i, gender, "01" + (300000000 + new Random().nextInt(699999999)), "Dhaka", dept, qual, dept, desig, "BMDC-" + (60000 + new Random().nextInt(10000)), "9am-5pm", new Date(), "Active"));
            }
        }
    }

    private void initializePatientData() {
        patients.clear();
        nextPatientId = 1;
        try {

            admitPatient(new Patient(getNextPatientId(), "Karim Ahmed", 35, "Male", "01555123456", "Mirpur", "A+", "Chest Pain", "Cardiology", "D101", "CCU-401-1", new Date(), "Rahim Ahmed", "Cash", 5000));
            admitPatient(new Patient(getNextPatientId(), "Fatima Khatun", 28, "Female", "01666098733", "Dhanmondi", "O+", "Pregnancy Checkup", "Obstetrics & Gynecology", "D221", "PC-201-1", new Date(), "Aslam Khan", "Card", 10000));
            admitPatient(new Patient(getNextPatientId(), "Jamal Uddin", 50, "Male", "01333432622", "Uttara", "B+", "Broken Arm", "Orthopedic Surgery", "D201", "GW-101-1", new Date(), "Kamal Uddin", "Mobile Banking", 3000));
            admitPatient(new Patient(getNextPatientId(), "Shireen Akter", 60, "Female", "01444345678", "Gulshan", "AB+", "Breathing Problem", "Cardiology", "D102", "ICU-301-1", new Date(), "Rezaul Akter", "Cash", 15000));
            admitPatient(new Patient(getNextPatientId(), "Babul Hawlader", 65, "Male", "01867890234", "Barishal", "B-", "High Fever", "General Medicine", "D213", "GW-102-1", new Date(), "Sonia Hawlader", "Cash", 1500));
            admitPatient(new Patient(getNextPatientId(), "Ayesha Siddika", 32, "Female", "01912347890", "Rajshahi", "AB+", "Maternity Complications", "Obstetrics & Gynecology", "D222", "PC-201-2", new Date(), "Raihan Kabir", "Insurance", 20000));
            admitPatient(new Patient(getNextPatientId(), "Harun Rashid", 45, "Male", "01787654321", "Narayanganj", "A-", "Severe Headache", "Neurology", "D225", "PC-202-1", new Date(), "Fatema Rashid", "Card", 7000));
            admitPatient(new Patient(getNextPatientId(), "Korban Ali", 70, "Male", "01698765432", "Gazipur", "O-", "Joint Pain", "Orthopedic Surgery", "D202", "GW-101-2", new Date(), "Korim Ali", "Cash", 2500));
            admitPatient(new Patient(getNextPatientId(), "Priya Sharma", 5, "Female", "01809876542", "Dhaka", "B+", "Viral Fever", "Pediatrics", "D209", "NICU-501-1", new Date(), "Rajat Sharma", "Mobile Banking", 4000));
            admitPatient(new Patient(getNextPatientId(), "Selina Parvin", 55, "Female", "01956789065", "Mymensingh", "A+", "High Blood Pressure", "Cardiology", "D103", "CCU-401-2", new Date(), "Jahangir Alam", "Insurance", 9000));


            int ptd1 = getNextPatientId();
            admitPatient(new Patient(ptd1, "Rofiqul Islam", 42, "Male", "01777456789", "Mohammadpur", "O-", "Stomach Pain", "Gastroenterology", "D245", "GW-101-3", new Date(), "Selim Islam", "Card", 2000));
            dischargePatient(ptd1);
            int ptd2 = getNextPatientId();
            admitPatient(new Patient(ptd2, "Sultan Mahmud", 25, "Male", "01315615832", "Cumilla", "O-", "Food Poisoning", "Gastroenterology", "D246", "GW-102-3", new Date(), "Reza Mahmud", "Mobile Banking", 2500));
            dischargePatient(ptd2);
            int ptd3 = getNextPatientId();
            admitPatient(new Patient(ptd3, "Maria Gomes", 19, "Female", "01890373882", "Dhaka", "A+", "Appendicitis", "Orthopedic Surgery", "D203", "PC-202-2", new Date(), "David Gomes", "Insurance", 30000));
            dischargePatient(ptd3);
            int ptd4 = getNextPatientId();
            admitPatient(new Patient(ptd4, "Nur Alam", 3, "Male", "01728376431", "Noakhali", "AB-", "Pneumonia", "Pediatrics", "D211", "NICU-501-2", new Date(), "Mostofa Kamal", "Cash", 8000));
            dischargePatient(ptd4);
            int ptd5 = getNextPatientId();
            admitPatient(new Patient(ptd5, "Jahanara Begum", 68, "Female", "01911223344", "Dhaka", "B+", "Diabetes Checkup", "Endocrinology", "D253", "PC-201-3", new Date(), "Anwar Hossain", "Cash", 1200));
            dischargePatient(ptd5);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void initializeStaffData() {
        staff.clear();
        nextStaffId = 501;

        // 1. Nurse
        addStaff(new Staff("S501", "Priya Saha", 25, "Female", "01898987667", "Dhaka", "Nurse", "General Ward", "Night"));
        addStaff(new Staff("S502", "Sultana Begum", 29, "Female", "01315615832", "Dhaka", "Nurse", "ICU", "Morning"));
        addStaff(new Staff("S503", "Rina Akter", 27, "Female", "01711223344", "Dhaka", "Nurse", "Pediatrics", "Evening"));
        addStaff(new Staff("S504", "Monirul Islam", 30, "Male", "01922334455", "Dhaka", "Nurse", "CCU", "Night"));
        addStaff(new Staff("S505", "Fatema Khanom", 26, "Female", "01533445566", "Dhaka", "Nurse", "Orthopedics", "Morning"));

        // 2. Lab Technician
        addStaff(new Staff("S506", "Anisur Rahman", 30, "Male", "01756634413", "Dhaka", "Lab Technician", "Pathology", "Morning"));
        addStaff(new Staff("S507", "Sumon Das", 28, "Male", "01844556677", "Dhaka", "Lab Technician", "Pathology", "Evening"));
        addStaff(new Staff("S508", "Nasrin Akter", 32, "Female", "01955667788", "Dhaka", "Lab Technician", "Microbiology", "Night"));
        addStaff(new Staff("S509", "Biplob Chowdhury", 29, "Male", "01666778899", "Dhaka", "Lab Technician", "Biochemistry", "Morning"));
        addStaff(new Staff("S510", "Tania Ahmed", 27, "Female", "01377889900", "Dhaka", "Lab Technician", "Pathology", "Evening"));

        // 3. Ward Boy
        addStaff(new Staff("S511", "Kamal Hossain", 40, "Male", "01923324545", "Dhaka", "Ward Boy", "General Ward", "Morning"));
        addStaff(new Staff("S512", "Jamal Sheikh", 35, "Male", "01788990011", "Dhaka", "Ward Boy", "Orthopedics", "Night"));
        addStaff(new Staff("S513", "Rahim Mia", 42, "Male", "01899001122", "Dhaka", "Ward Boy", "ICU", "Evening"));
        addStaff(new Staff("S514", "Selim Khan", 38, "Male", "01600112233", "Dhaka", "Ward Boy", "General Ward", "Morning"));
        addStaff(new Staff("S515", "Harunur Rashid", 45, "Male", "01511223344", "Dhaka", "Ward Boy", "Private Cabin", "Night"));

        // 4. Billing Officer
        addStaff(new Staff("S516", "Mizanur Chowdhury", 35, "Male", "01509876541", "Dhaka", "Billing Officer", "Accounts", "Morning"));
        addStaff(new Staff("S517", "Farhana Zaman", 30, "Female", "01722334455", "Dhaka", "Billing Officer", "Accounts", "Evening"));
        addStaff(new Staff("S518", "Abdullah Al Masud", 33, "Male", "01833445566", "Dhaka", "Billing Officer", "Front Desk", "Morning"));
        addStaff(new Staff("S519", "Rehana Parvin", 28, "Female", "01944556677", "Dhaka", "Billing Officer", "Accounts", "Evening"));
        addStaff(new Staff("S520", "Saiful Islam", 36, "Male", "01355667788", "Dhaka", "Billing Officer", "Billing", "Night"));

        // 5. Receptionist
        addStaff(new Staff("S521", "Farida Yasmin", 28, "Female", "01698766782", "Dhaka", "Receptionist", "Front Desk", "Evening"));
        addStaff(new Staff("S522", "Sadia Islam", 24, "Female", "01766778899", "Dhaka", "Receptionist", "Front Desk", "Morning"));
        addStaff(new Staff("S523", "Raihan Ahmed", 26, "Male", "01877889900", "Dhaka", "Receptionist", "Emergency", "Night"));
        addStaff(new Staff("S524", "Jannatul Ferdous", 25, "Female", "01988990011", "Dhaka", "Receptionist", "Front Desk", "Evening"));
        addStaff(new Staff("S525", "Imran Hossain", 27, "Male", "01599001122", "Dhaka", "Receptionist", "Information Desk", "Morning"));

        // 6. Security Guard
        addStaff(new Staff("S526", "Abul Kashem", 45, "Male", "01711223344", "Dhaka", "Security Guard", "Security", "Night"));
        addStaff(new Staff("S527", "Bashir Ahmed", 50, "Male", "01822334455", "Dhaka", "Security Guard", "Security", "Morning"));
        addStaff(new Staff("S528", "Jahangir Alam", 48, "Male", "01933445566", "Dhaka", "Security Guard", "Security", "Evening"));
        addStaff(new Staff("S529", "Sohrab Hossain", 52, "Male", "01644556677", "Dhaka", "Security Guard", "Security", "Night"));
        addStaff(new Staff("S530", "Ismail Khan", 47, "Male", "01355667788", "Dhaka", "Security Guard", "Security", "Morning"));

        // 7. Pharmacist
        addStaff(new Staff("S531", "Rina Barua", 35, "Female", "01755555555", "Dhaka", "Pharmacist", "Pharmacy", "Morning"));
        addStaff(new Staff("S532", "Al-Amin Hossain", 32, "Male", "01866666666", "Dhaka", "Pharmacist", "Pharmacy", "Evening"));
        addStaff(new Staff("S533", "Sumaiya Akter", 30, "Female", "01977777777", "Dhaka", "Pharmacist", "Pharmacy", "Night"));
        addStaff(new Staff("S534", "David Mandal", 40, "Male", "01688888888", "Dhaka", "Pharmacist", "Pharmacy", "Morning"));
        addStaff(new Staff("S535", "Firoz Mahmud", 33, "Male", "01399999999", "Dhaka", "Pharmacist", "Pharmacy", "Evening"));

        // 8. Ambulance Driver
        addStaff(new Staff("S536", "Abdul Jalil", 42, "Male", "01712341234", "Dhaka", "Ambulance Driver", "Transport", "Night"));
        addStaff(new Staff("S537", "Kamrul Hasan", 38, "Male", "01823452345", "Dhaka", "Ambulance Driver", "Transport", "Morning"));
        addStaff(new Staff("S538", "Mostafa Kamal", 45, "Male", "01934563456", "Dhaka", "Ambulance Driver", "Transport", "Evening"));
        addStaff(new Staff("S539", "Sirajul Islam", 40, "Male", "01645674567", "Dhaka", "Ambulance Driver", "Transport", "Night"));
        addStaff(new Staff("S540", "Anwar Parvez", 39, "Male", "01556785678", "Dhaka", "Ambulance Driver", "Transport", "Morning"));
    }

    private void initializeAmbulanceData() {
        ambulances.clear();
        ambulances.add(new Ambulance("DH-GA-11-1111", "Available"));
        ambulances.add(new Ambulance("DH-GA-11-2222", "On Duty"));
        ambulances.add(new Ambulance("DH-GA-11-3333", "Under Maintenance"));
        ambulances.add(new Ambulance("CH-GA-15-4444", "Available"));
        ambulances.add(new Ambulance("DH-GA-12-5555", "Available"));
        ambulances.add(new Ambulance("DH-GA-12-6666", "On Duty"));
        ambulances.add(new Ambulance("DH-GA-12-7777", "Available"));
        ambulances.add(new Ambulance("DH-GA-13-8888", "Available"));
        ambulances.add(new Ambulance("SY-GA-11-9999", "On Duty"));
        ambulances.add(new Ambulance("KH-GA-14-1010", "Under Maintenance"));
        ambulances.add(new Ambulance("RJ-GA-16-2020", "Available"));
        ambulances.add(new Ambulance("DH-HA-18-3030", "On Duty"));
        ambulances.add(new Ambulance("DH-HA-18-4040", "Available"));
        ambulances.add(new Ambulance("DH-HA-19-5050", "Available"));
    }

    private void initializeAuthorityData() {
        authorities.clear();
        nextAuthorityId = 101;

        addAuthority(new Authority(getNextAuthorityId(), "Prof. Dr. Abul Kalam", 65, "Male", "01711000001", "Gulshan, Dhaka", "Hospital Director", "Administration", "director@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mr. Anisul Haque", 58, "Male", "01811000002", "Banani, Dhaka", "Chairman", "Board of Directors", "chairman@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mrs. Farida Yasmin", 45, "Female", "01911000003", "Dhanmondi, Dhaka", "Hospital Administrator", "Administration", "admin@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mr. Kamal Uddin", 42, "Male", "01611000004", "Uttara, Dhaka", "HR Manager", "Human Resources", "hr@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mr. Moniruzzaman", 50, "Male", "01511000005", "Mirpur, Dhaka", "Finance Manager", "Finance & Accounts", "finance@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Dr. Brig. Gen. (Ret.) Hamidur Rahman", 62, "Male", "01711000006", "Cantonment, Dhaka", "Chief Medical Officer (CMO)", "Medical Administration", "cmo@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Dr. A. K. Azad", 45, "Male", "01711383926", "Dhaka", "Head of Department (HoD)", "Cardiology", "hod.cardiology@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mrs. Amina Begum", 55, "Female", "01811000007", "Mohammadpur, Dhaka", "Nursing Superintendent", "Nursing", "nursing.super@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mr. Rafiqul Islam", 48, "Male", "01911000008", "Tejgaon, Dhaka", "Facility Manager", "Maintenance & Facility", "facility@iit-hospital.com", new Date(), "Active"));
        addAuthority(new Authority(getNextAuthorityId(), "Mr. Sohel Rana", 44, "Male", "01611000009", "Badda, Dhaka", "Security Head", "Security", "security@iit-hospital.com", new Date(), "Active"));
    }

    private void addWardWithBeds(String ward, String room, int count) {
        bedsByWard.putIfAbsent(ward, new ArrayList<>());
        for (int i = 1; i <= count; i++) {
            bedsByWard.get(ward).add(new Bed(room, room + "-" + i, ward));
        }
    }

    public void admitPatient(Patient p) throws Exception {
        Bed b = findBed(p.getBedKey());
        if (b != null && b.isVacant) {
            b.isVacant = false;
            patients.put(p.getPatientId(), p);
        } else {
            throw new Exception("Bed " + p.getBedKey() + " is not available.");
        }
    }

    public void dischargePatient(int patientId) {
        Patient p = patients.get(patientId);
        if (p != null) {
            p.setDischarged(true);
            Bed b = findBed(p.getBedKey());
            if (b != null) {
                b.isVacant = true;
            }
        }
    }

    private Bed findBed(String key) {
        return bedsByWard.values().stream()
                .flatMap(List::stream)
                .filter(b -> b.bedKey.equals(key))
                .findFirst()
                .orElse(null);
    }

    public List<Bed> getVacantBedsByRoom(String roomNo) {
        return bedsByWard.values().stream()
                .flatMap(List::stream)
                .filter(b -> b.roomNo.equals(roomNo) && b.isVacant)
                .collect(Collectors.toList());
    }

    public void addDoctor(Doctor d) {
        doctors.put(d.getDoctorId(), d);
    }

    public String getNextDoctorId() {
        return "D" + nextDoctorId++;
    }

    public int getNextPatientId() {
        return nextPatientId++;
    }

    public void addStaff(Staff s) {
        staff.put(s.getStaffId(), s);
    }

    public String getNextStaffId() {
        return "S" + nextStaffId++;
    }

    public void addAuthority(Authority authority) {
        authorities.put(authority.getAuthorityId(), authority);
    }

    public String getNextAuthorityId() {
        return "A" + nextAuthorityId++;
    }

    private static HospitalData loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            HospitalData data = (HospitalData) ois.readObject();
            if (data.ambulances == null) {
                data.ambulances = new ArrayList<>();
            }
            if (data.authorities == null) {
                data.authorities = new LinkedHashMap<>();
            }
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Initializing with default data.");
            HospitalData data = new HospitalData();
            data.initializeDefaultData();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not load data. Initializing with default data.");
            HospitalData data = new HospitalData();
            data.initializeDefaultData();
            return data;
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String user, String pass) {
        return "admin".equals(user) && "admin123".equals(pass);
    }
}