package com.example.hmsAdmin.service;

import com.example.hmsAdmin.dto.requestDto.UsersRequest;
import com.example.hmsAdmin.dto.responseDto.BaseApiResponse;

import com.example.hmsAdmin.dto.responseDto.UsersResponse;
import com.example.hmsAdmin.entity.*;
import com.example.hmsAdmin.implementation.UsersImpl;
import com.example.hmsAdmin.repository.DoctorRepository;
import com.example.hmsAdmin.repository.NurseRepository;
import com.example.hmsAdmin.repository.PatientRepository;
import com.example.hmsAdmin.repository.UsersRepository;
import com.example.hmsAdmin.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@Service
public class UsersService implements UsersImpl {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private NurseRepository nurseRepository;
    @Autowired
    private ReceptionistRepository receptionistRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional // Ensure this method is wrapped in a transaction
    public BaseApiResponse createOrUpdateUsers(List<UsersRequest> userRequests) {
        List<Users> userList = new ArrayList<>();
        List<Patient> patientList = new ArrayList<>();
        List<Doctor> doctorList = new ArrayList<>();
        List<Nurse> nurseList = new ArrayList<>();
        List<Receptionist> receptionistList = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        try {
            for (UsersRequest request : userRequests) {
                // Validate request
                if (request.getEmail() == null || request.getEmail().isEmpty()) {
                    errorMessages.add("Email cannot be empty for user: " + request.getUsername());
                    continue;
                }
                if (request.getUsername() == null || request.getUsername().isEmpty()) {
                    errorMessages.add("Username cannot be empty for user: " + request.getEmail());
                    continue;
                }

                // Find existing user
                Users user = null;
                if (request.getUserId() != null) {
                    user = usersRepository.findById(request.getUserId()).orElse(null);
                }
                if (user == null) {
                    user = usersRepository.findByEmail(request.getEmail());
                }
                if (user == null) {
                    user = usersRepository.findByUsername(request.getUsername());
                }

                // Create new user if not found
                if (user == null) {
                    user = new Users();
                    user.setUserId(request.getUserId());  // Make sure userId is set if new user
                }

                // Set common fields
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setStatus(request.getStatus());
                user.setRole(request.getRole());

                userList.add(user);  // Add user to the list for batch save

                // Ensure userId is passed to the role-specific entities
                switch (request.getRole()) {
                    case "Patient":
                        Patient patient = patientRepository.findByUserId(user.getUserId());
                        if (patient == null) {
                            patient = new Patient();
                            patient.setUserId(user.getUserId());  // Set userId for the Patient
                        }
                        patient.setEmail(request.getEmail());
                        patient.setName(request.getUsername());
                        patient.setPassword(passwordEncoder.encode(request.getPassword()));
                        patient.setStatus(request.getStatus());
                        patientList.add(patient);
                        break;

                    case "Doctor":
                        Doctor doctor = doctorRepository.findByUserId(user.getUserId());
                        if (doctor == null) {
                            doctor = new Doctor();
                            doctor.setUserId(user.getUserId());  // Set userId for the Doctor
                        }
                        doctor.setEmail(request.getEmail());
                        doctor.setName(request.getUsername());
                        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
                        doctor.setStatus(request.getStatus());
                        doctorList.add(doctor);
                        break;

                    case "Nurse":
                        Nurse nurse = nurseRepository.findByUserId(user.getUserId());
                        if (nurse == null) {
                            nurse = new Nurse();
                            nurse.setUserId(user.getUserId());  // Set userId for the Nurse
                        }
                        nurse.setEmail(request.getEmail());
                        nurse.setName(request.getUsername());
                        nurse.setPassword(passwordEncoder.encode(request.getPassword()));
                        nurse.setStatus(request.getStatus());
                        nurseList.add(nurse);
                        break;

                    case "Receptionist":
                        Receptionist receptionist = receptionistRepository.findByUserId(user.getUserId());
                        if (receptionist == null) {
                            receptionist = new Receptionist();
                            receptionist.setUserId(user.getUserId());  // Set userId for the Receptionist
                        }
                        receptionist.setEmail(request.getEmail());
                        receptionist.setName(request.getUsername());
                        receptionist.setPassword(passwordEncoder.encode(request.getPassword()));
                        receptionist.setStatus(request.getStatus());
                        receptionistList.add(receptionist);
                        break;

                    default:
                        break;
                }
            }

            // Save all entities in batch
            usersRepository.saveAll(userList);
            patientRepository.saveAll(patientList);
            doctorRepository.saveAll(doctorList);
            nurseRepository.saveAll(nurseList);
            receptionistRepository.saveAll(receptionistList);

            // If there are any error messages, return partial success response
            if (!errorMessages.isEmpty()) {
                return new BaseApiResponse(PARTIAL_STATUS, FAILURE, PARTIAL_DATA, Collections.emptyList());
            }

            return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_CREATION, userList);
        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, e.getMessage(), Collections.emptyList());
        }
    }

//
//    @Transactional // Ensure this method is wrapped in a transaction
//    public BaseApiResponse createOrUpdateUsers(List<UsersRequest> userRequests) {
//        List<Users> userList = new ArrayList<>();
//        List<Patient> patientList = new ArrayList<>();
//        List<Doctor> doctorList = new ArrayList<>();
//        List<Nurse> nurseList = new ArrayList<>();
//        List<Receptionist> receptionistList = new ArrayList<>();
//        List<String> errorMessages = new ArrayList<>();
//
//        try {
//            for (UsersRequest request : userRequests) {
//                // Validate request
//                if (request.getEmail() == null || request.getEmail().isEmpty()) {
//                    errorMessages.add("Email cannot be empty for user: " + request.getUsername());
//                    continue;
//                }
//                if (request.getUsername() == null || request.getUsername().isEmpty()) {
//                    errorMessages.add("Username cannot be empty for user: " + request.getEmail());
//                    continue;
//                }
//                // Check for existing user by userId, email, or username
//                Users user = null;
//                if (request.getUserId() != null) {
//                    user = usersRepository.findById(request.getUserId()).orElse(null);
//                }
//                if (user == null) {
//                    user = usersRepository.findByEmail(request.getEmail());
//                }
//                if (user == null) {
//                    user = usersRepository.findByUsername(request.getUsername());
//                }
//                // Create new user if not found
//                if (user == null) {
//                    user = new Users();
//                }
//
//                // Create/Update user fields
//                user.setUsername(request.getUsername());
//                user.setEmail(request.getEmail());
//                user.setPassword(passwordEncoder.encode(request.getPassword()));
//                user.setStatus(request.getStatus());
//                user.setRole(request.getRole());
//                userList.add(user);
//
//                // Handle patient creation/updating
//                if ("Patient".equalsIgnoreCase(request.getRole())) {
//                    Patient patient = patientRepository.findByUserId(user.getUserId());
//                    if (patient == null) {
//                        patient = new Patient();
//                        patient.setUserId(user.getUserId());
//                    }
//                    // Update patient fields
//                    patient.setEmail(request.getEmail());
//                    patient.setName(request.getUsername());
//                    patient.setPassword(passwordEncoder.encode(request.getPassword()));
//                    patient.setStatus(request.getStatus());
//                    patientList.add(patient);
//                }
//
//                // Handle doctor creation/updating
//                if ("Doctor".equalsIgnoreCase(request.getRole())) {
//                    Doctor doctor = doctorRepository.findByUserId(user.getUserId());
//                    if (doctor == null) {
//                        doctor = new Doctor();
//                        doctor.setUserId(user.getUserId());
//                    }
//                    // Update patient fields
//                    doctor.setEmail(request.getEmail());
//                    doctor.setName(request.getUsername());
//                    doctor.setPassword(passwordEncoder.encode(request.getPassword()));
//                    doctor.setStatus(request.getStatus());
//                    doctorList.add(doctor);
//                }
//
//                // Handle nurse creation/updating
//                if ("Nurse".equalsIgnoreCase(request.getRole())) {
//                    Nurse nurse = nurseRepository.findByUserId(user.getUserId());
//                    if (nurse == null) {
//                        nurse = new Nurse();
//                        nurse.setUserId(user.getUserId());
//                    }
//                    // Update nurse fields
//                    nurse.setEmail(request.getEmail());
//                    nurse.setName(request.getUsername());
//                    nurse.setPassword(passwordEncoder.encode(request.getPassword()));
//                    nurse.setStatus(request.getStatus());
//                    nurseList.add(nurse);
//                }
//
//                // Handle receptionist creation/updating
//                if ("Receptionist".equalsIgnoreCase(request.getRole())) {
//                    Receptionist receptionist = receptionistRepository.findByUserId(user.getUserId());
//                    if (receptionist == null) {
//                        receptionist = new Receptionist();
//                        receptionist.setUserId(user.getUserId());
//                    }
//                    // Update nurse fields
//                    receptionist.setEmail(request.getEmail());
//                    receptionist.setName(request.getUsername());
//                    receptionist.setPassword(passwordEncoder.encode(request.getPassword()));
//                    receptionist.setStatus(request.getStatus());
//                    receptionistList.add(receptionist);
//                }
//            }
//
//            // Save all users at once
//            usersRepository.saveAll(userList);
//            patientRepository.saveAll(patientList);
//            doctorRepository.saveAll(doctorList);
//            nurseRepository.saveAll(nurseList);
//            receptionistRepository.saveAll(receptionistList);
//
//            if (!errorMessages.isEmpty()) {
//                return new BaseApiResponse(PARTIAL_STATUS, FAILURE, PARTIAL_DATA, Collections.emptyList());
//            }
//
//            return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_CREATION, userList);
//        } catch (Exception e) {
//            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, e.getMessage(), Collections.emptyList());
//        }
//    }

//    -----------------------------------------------------------------------------------------------------------

//    // Method to get all users or get user by username or id or email

    @Transactional // Ensure this method is wrapped in a transaction
    public BaseApiResponse fetchUsers(Long userId, String username, String email,boolean findAll,String role) {
        List<Users> users = new ArrayList<>();

        // Fetch users based on provided parameters
        if (userId != null) {
            users.add(usersRepository.findById(userId).orElse(null));
        }
        if (username != null) {
            users.addAll(usersRepository.findByUsernameIn(Collections.singletonList(username)));
        }
        if (email != null) {
            users.addAll(usersRepository.findByEmailIn(Collections.singletonList(email)));
        }
        if (role != null) {
            users.addAll(usersRepository.findByRoleIn(Collections.singletonList(role)));
        }

        // If no parameters were provided, fetch all users
        if (findAll) {
            users.addAll(usersRepository.findAll());
        }
        users.removeIf(Objects::isNull); // Remove null entries

        if (users.isEmpty()) {
            return new BaseApiResponse(BAD_REQUEST, FAILURE, NOT_PRESENT, Collections.emptyList());
        }
        List<UsersResponse> responseDto = users.stream()
                .map(user -> new UsersResponse(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatus(),
                        user.getRole()))
                .collect(Collectors.toList());

        return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, responseDto);
    }

}




