package com.lqt.controllers;

import com.lqt.dto.*;
import com.lqt.pojo.Group;
import com.lqt.pojo.Role;
import com.lqt.pojo.Survey;
import com.lqt.pojo.User;
import com.lqt.service.*;
import com.lqt.util.Routing;
import com.lqt.util.SurveyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lqt.util.Constants.PAGE_SIZE;

@Controller
public class AdminController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userDetailServiceImpl;
    @Autowired
    private GroupService groupService;
    @Autowired
    private PostService postService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private SurveyService surveyService;

    @GetMapping(Routing.ADMIN_LOGIN)
    public String loginAdmin(Model model, @RequestParam(value = "logout", required = false) String logout,@RequestParam(value = "error", required = false) String error, HttpServletResponse response) {
        if (logout != null) {
            Cookie cookie = new Cookie("JWT_TOKEN", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            SecurityContextHolder.clearContext();
            model.addAttribute("success", "Logout successfully!");
            return "login";
        }
        if (error != null) {
            model.addAttribute("error", "Username or password is invalid");
        }
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping(Routing.ADMIN_LOGIN)
    public String processLoginAdmin(@ModelAttribute("loginDto") LoginDto loginDto,
                                    HttpServletResponse response,
                                    RedirectAttributes attributes) throws Exception {
        try {
            authenticate(loginDto.getUsername(), loginDto.getPassword());

            final UserDetails userDetails = userDetailServiceImpl
                    .loadUserByUsername(loginDto.getUsername());
            User user = userDetailServiceImpl.findByUsername(userDetails.getUsername());
            List<Role> roles = userDetailServiceImpl.getAllRoleOfUser(user.getId());
            Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
            if (!hasAdminRole) {
                attributes.addFlashAttribute("error", "You don not have permission to access this page");
                return "redirect:/admin/login";
            }
            JwtResponse jwtResponse = authService.login(userDetails);
            if (jwtResponse != null) {
                Cookie cookie = new Cookie("JWT_TOKEN", jwtResponse.getAccessToken());
                cookie.setPath("/");
                cookie.setMaxAge(3600);

                response.addCookie(cookie);
                return "redirect:/admin";
            } else {
                return "redirect:/admin/login";
            }
        }catch (BadCredentialsException e) {
            return "redirect:/admin/login?error";
        }
    }

    private void authenticate(String username, String password) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping(Routing.ADMIN)
    public String index(Model model, @RequestParam(required = false) Map<String, String> params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            List<StatsUserResponse> userResponses = statisticService.statsUsers();
            model.addAttribute("statsPostResponse", statisticService.statsNumberOfPosts(params));
            model.addAttribute("userResponses", userResponses);
            model.addAttribute("totalUsers", statisticService.countAllUsers());
            model.addAttribute("totalAlumni", userDetailServiceImpl.getAllAlumni().size());
            model.addAttribute("totalPosts", statisticService.countNumberOfPostsWithoutParam());
            model.addAttribute("totalGroups", statisticService.countAllGroups());
            return "index";
        } else {
            return "redirect:/admin/login";
        }
    }

    @GetMapping(Routing.ADMIN_USERS)
    public String getAllUsers(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        List<UserDto> userDtos = userDetailServiceImpl.getAllUsers();
        int totalRecords = userDtos.size();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalRecords);
        List<UserDto> currentPageRecords = userDtos.subList(startIndex, endIndex);
        model.addAttribute("users", currentPageRecords);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "user";
    }

    @GetMapping(Routing.ADMIN_GROUPS)
    public String getAllGroups(Model model,
                               @RequestParam(value = "name", required = false, defaultValue = "") String name,
                               @RequestParam(value = "page", required = false, defaultValue = "1")int page) {

        List<Group> groups = groupService.getAllGroups(name);
        int totalRecords = groups.size();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalRecords);

        List<Group> currentPageRecords = groups.subList(startIndex, endIndex);
        Map<Long, Integer> numOfUsers = new HashMap<>();
        groups.forEach(g -> {
            int numOfUser = statisticService.countNumberOfUserInGroup(g.getId());
            numOfUsers.put(g.getId(), numOfUser);
        });

        model.addAttribute("groups", currentPageRecords);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("numOfUsers", numOfUsers);
        return "group";
    }

    @GetMapping(Routing.ADMIN_GROUPS_ADD)
    public String addNewGroup(Model model) {
        model.addAttribute("group", new Group());
        return "add-group";
    }


    @GetMapping(Routing.ADMIN_GROUPS_BY_ID)
    public String editGroup(@PathVariable("groupId") Long groupId, Model model) {
        Group group = groupService.findById(groupId);
        model.addAttribute("group", group);
        model.addAttribute("groupId", groupId);
        return "edit-group";
    }

    @PostMapping(Routing.ADMIN_GROUPS_BY_ID)
    public String executeEditGroup(@PathVariable("groupId") Long groupId, @ModelAttribute("group") Group group, RedirectAttributes attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            Group groupUpdated = groupService.update(group, groupId, currentUser.getId());
            if (groupUpdated != null) {
                attributes.addFlashAttribute("success", "Update group successfully!");
            } else {
                attributes.addFlashAttribute("error", "Update group failed! Please try later.");
            }
            return "redirect:/admin/groups";
        }
        attributes.addFlashAttribute("error", "Update group failed! Please try later.");
        return "redirect:/admin/groups";
    }


    @GetMapping(Routing.ADMIN_GROUPS_MEMBERS)
    public String getUsersOfGroup(@PathVariable("groupId") Long groupId,
                                  Model model,
                                  @RequestParam(value = "page", required = false, defaultValue = "1")int page) {
        List<UserDto> userDtos = groupService.getAllUsersOfGroup(groupId);
        int totalRecords = userDtos.size();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalRecords);
        List<UserDto> currentPageRecords = userDtos.subList(startIndex, endIndex);
        model.addAttribute("users", currentPageRecords);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("groupId", groupId);
        return "group-user";
    }

    @PostMapping(Routing.ADMIN_GROUPS)
    public String executeAddNewGroup(RedirectAttributes attributes, @ModelAttribute("group") Group group) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            Group groupSaved = groupService.create(group, currentUser.getId());
            if (groupSaved != null) {
                attributes.addFlashAttribute("success", "Create group successfully!");
            } else {
                attributes.addFlashAttribute("error", "Create group failed! Please try later.");
            }
            return "redirect:/admin/groups";
        }
        attributes.addFlashAttribute("error", "Create group failed! Please try later.");
        return "redirect:/admin/groups";
    }

    @GetMapping(Routing.ADMIN_POSTS)
    public String getAllPosts(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        List<PostDto> postDtos = postService.getAllPosts();
        int totalRecords = postDtos.size();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalRecords);
        List<PostDto> currentPageRecords = postDtos.subList(startIndex, endIndex);

        Map<Long, Integer> interactions = new HashMap<>();
        Map<Long, Integer> numberOfComments = new HashMap<>();
        postDtos.forEach(p -> {
            int likes = statisticService.countInteractOfPost(p.getId());
            int comments = statisticService.countCommentOfPost(p.getId());
            interactions.put(p.getId(), likes);
            numberOfComments.put(p.getId(), comments);
        });
        model.addAttribute("posts", currentPageRecords);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("interactions", interactions);
        model.addAttribute("numberOfComments", numberOfComments);
        return "post";
    }


    @GetMapping(Routing.ADMIN_USERS_BY_ID)
    public String getUserById(@PathVariable("userId") Long userId, Model model) {
        UserDto userDto = userDetailServiceImpl.findUserById(userId);
        model.addAttribute("user", userDto);
        return "user-detail";
    }

    @GetMapping(Routing.ADMIN_USERS_ALUMNI)
    public String getAllAlumniIsNotConfirmed(Model model) {
        List<AlumniResponse> alumniResponses = userDetailServiceImpl.getAllAlumniIsNotConfirmed();
        model.addAttribute("alumniList", alumniResponses);
        return "user";
    }

    //    SURVEY
    @GetMapping(Routing.ADMIN_SURVEYS)
    public String getAllSurveys(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        List<SurveyDto> surveyDtos = surveyService.getAllSurveys();
        int totalRecords = surveyDtos.size();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalRecords);
        List<SurveyDto> currentPageRecords = surveyDtos.subList(startIndex, endIndex);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("surveys", currentPageRecords);
        return "survey";
    }

    @GetMapping(Routing.ADMIN_SURVEYS_BY_ID)
    public String editSurvey(@PathVariable("surveyId") Long surveyId, Model model) {
        SurveyDto surveyDto = surveyService.getSurveyById(surveyId);
        model.addAttribute("survey", surveyDto);
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("surveyType", SurveyType.values());
        return "edit-survey";
    }

    @PostMapping(Routing.ADMIN_SURVEYS_BY_ID)
    public String executeEditSurvey(@PathVariable("surveyId") Long surveyId, @ModelAttribute("survey") SurveyDto surveyDto, RedirectAttributes attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userDetailServiceImpl.getMyAccount(userDetails.getUsername());
            SurveyDto surveyDtoUpdated = surveyService.update(surveyDto, surveyId, currentUser.getId());
            if (surveyDtoUpdated != null) {
                attributes.addFlashAttribute("success", "Update survey successfully!");
            } else {
                attributes.addFlashAttribute("error", "Update survey failed! Please try later.");
            }
            return "redirect:/admin/surveys";
        }
        attributes.addFlashAttribute("error", "Update survey failed! Please try later.");
        return "redirect:/admin/surveys";
    }

//    Notification
    @GetMapping(Routing.ADMIN_SEND_NOTIFICATION_ALL)
    public String sendNotificationToAllAlumniView() {
        return "send-notification";
    }

    @GetMapping(Routing.ADMIN_SEND_NOTIFICATION_IN_GROUP)
    public String sendNotificationToAllAlumniInGroupView(Model model, @PathVariable("groupId") Long groupId) {
        model.addAttribute("groupId", groupId);
        return "send-notification";
    }

}
