package com.blogging.blog.service;

import com.blogging.blog.domain.Admin;
import com.blogging.blog.dto.request.AdminRequestDTO;
import com.blogging.blog.dto.request.AdminUpdateDTO;
import com.blogging.blog.dto.request.LoginDTO;
import com.blogging.blog.dto.response.AdminResponseDTO;
import com.blogging.blog.exception.PasswordAndEmailNotFound;
import com.blogging.blog.exception.ResourcesFoundException;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.repository.AdminRepository;
import com.blogging.blog.service.iservices.AdminServices;
import com.blogging.blog.utility.EntityMapper;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AdminServicesImpl implements AdminServices {
    private final AdminRepository adminRepository;
    private final EntityMapper entityMapper;
    private HttpSession session;

    public AdminServicesImpl(AdminRepository adminRepository, EntityMapper entityMapper, HttpSession session) {
        this.adminRepository = adminRepository;
        this.entityMapper = entityMapper;
        this.session = session;
    }

    @Override
    public GenericResponse registerAdmin(AdminRequestDTO requestDTO) {
        Optional<Admin> adminOptional = adminRepository.findByEmail(requestDTO.getEmail());
        if (adminOptional.isPresent())
        {
            throw new ResourcesFoundException("the admin already exits", HttpStatus.ALREADY_REPORTED);
        }
        Admin admin = Admin.builder().name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .build();

        Admin savedAdmin =  adminRepository.save(admin);
        AdminResponseDTO adminResponseDTO = AdminResponseDTO.builder().AdminId(savedAdmin.getId()).name(savedAdmin.getName())
                .email(savedAdmin.getEmail()).build();
        return GenericResponse.builder().message("Request Successful").status("00").httpStatus(HttpStatus.CREATED)
                .data(adminResponseDTO).build();
    }
    @Override
    public GenericResponse loginAdmin(LoginDTO loginDTO, HttpServletRequest request)
    {
       Optional<Admin>optionalAdmin = adminRepository.findByEmail(loginDTO.getEmail());
       if (optionalAdmin.isEmpty())
       {
           return GenericResponse.builder().message("Invalid Request").status("01").httpStatus(HttpStatus.BAD_REQUEST)
                   .build();
       }
        Admin admin = optionalAdmin.get();
        if (!loginDTO.getPassword().equals(admin.getPassword()))
        {
            throw new PasswordAndEmailNotFound("please check your email or password", HttpStatus.NOT_FOUND);
        }

        AdminResponseDTO adminResponseDTO = AdminResponseDTO.builder().AdminId(admin.getId())
                .name(admin.getName()).email(admin.getEmail()).build();
        session = request.getSession();
        session.setAttribute("adminId",admin.getId());
        //Successfully logged in
        return GenericResponse.builder().message("Request Successful").httpStatus(HttpStatus.OK).status("00")
                .data(adminResponseDTO).build();
    }

    @Override
    public AdminResponseDTO updateAdmin(AdminUpdateDTO request) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            throw new ResourcesNotFoundException("Please log in first", HttpStatus.FORBIDDEN);
        }
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isEmpty())
        {
            throw  new ResourcesNotFoundException("Admin not found", HttpStatus.NOT_FOUND);
        }
        //gut the admin
        Admin admin = optionalAdmin.get();
        //update fields
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        //save the admin back
        Admin updatedUser = adminRepository.save(admin);

        return AdminResponseDTO.builder().email(request.getEmail()).name(request.getName())
                .AdminId(updatedUser.getId()).build();
    }

    @Override
    public boolean isAdmin(Long adminId) {
        // Retrieve the currently logged-in admin ID from the session or security context
         adminId = (Long) session.getAttribute("adminId"); // Replace with the correct session attribute if needed

        // Check if an admin ID exists in the session or security context
        if (adminId != null) {
            // Check if the admin ID exists in the database
            Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
            return optionalAdmin.isPresent();
        }
        return false; // No admin ID found in the session
    }


}
