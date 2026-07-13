//package com.minku.expensetracker.controller;
//
//import java.util.Map;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import com.minku.expensetracker.dto.ExpenseRequestDto;
//import com.minku.expensetracker.entity.User;
//import com.minku.expensetracker.repository.UserRepository;
//import com.minku.expensetracker.service.BudgetService;
//import com.minku.expensetracker.service.ExpenseService;
//import com.minku.expensetracker.service.LoginService;
//import com.minku.expensetracker.service.UserService;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//public class MainViewController {
//
//    private final LoginService loginService;
//    private final UserService userService;
//    private final ExpenseService expenseService;
//    private final BudgetService budgetService;
//    private final UserRepository userRepository; 
//
//    // Constructor Injection
//    public MainViewController(LoginService loginService, UserService userService, 
//                              ExpenseService expenseService, BudgetService budgetService, UserRepository userRepository) {
//        this.loginService = loginService;
//        this.userService = userService;
//        this.expenseService = expenseService;
//        this.budgetService = budgetService;
//        this.userRepository = userRepository;
//    }
//
//    // ==========================================
//    // 🛡️ AUTHENTICATION & LOGIN WORKFLOW
//    // ==========================================
//
//    @GetMapping("/login")
//    public String showLoginPage(Model model) {
//        return "login";
//    }
//
//    @PostMapping("/auth/perform-login")
//    public String handleUiLogin(@RequestParam String email, @RequestParam String password, 
//                                 HttpSession session, Model model) {
//        try {
//            Map<String, String> authResult = loginService.login(email, password);
//
//            if ("0".equals(authResult.get("Status"))) {
//                model.addAttribute("error", authResult.get("Message"));
//                return "login";
//            }
//
//            // Real-time authentication session mapping
//            User actualUser = userRepository.findByEmail(email);
//            
//            session.setAttribute("userId", actualUser.getId()); 
//            session.setAttribute("userName", actualUser.getName());
//            session.setAttribute("userRole", actualUser.getRole());
//
//            return "redirect:/dashboard";
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Authentication error: " + e.getMessage());
//            return "login";
//        }
//    }
//
//    @GetMapping("/auth/logout")
//    public String handleLogout(HttpSession session) {
//        session.invalidate(); 
//        return "redirect:/login?message=Logged out successfully.";
//    }
//
//    // ==========================================
//    // 📝 USER REGISTRATION FRONTEND WORKFLOW
//    // ==========================================
//
//    @GetMapping("/auth/register")
//    public String showRegisterPage(Model model) {
//        return "register";
//    }
//
//    @PostMapping("/auth/perform-register")
//    public String handleUiRegister(@RequestParam String name, 
//                                   @RequestParam String email, 
//                                   @RequestParam String password,
//                                   Model model) {
//        try {
//            User newUser = new User();
//            newUser.setName(name);
//            newUser.setEmail(email);
//            newUser.setPassword(password);
//
//            String result = userService.createNewUser(newUser);
//
//            if (result.contains("Already Exist") || result.contains("Password must contain")) {
//                model.addAttribute("error", result);
//                return "register";
//            }
//
//            return "redirect:/login?message=" + result;
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Registration error: " + e.getMessage());
//            return "register";
//        }
//    }
//
//    // ==========================================
//    // 📊 100% DYNAMIC ANALYTICAL DASHBOARD WORKFLOW
//    // ==========================================
//
//    @GetMapping("/dashboard")
//    public String showDashboard(@RequestParam(value = "message", required = false) String successMessage, 
//                                HttpSession session, Model model) {
//        Long currentUserId = (Long) session.getAttribute("userId");
//        if (currentUserId == null) {
//            return "redirect:/login?error=Please login to access the environment.";
//        }
//
//        // Live status parameters initializing with plain zeros
//        double budgetLimit = 0.00; 
//        double totalSpent = 0.00;
//
//        // Fetching user associated budget dictionary maps natively from Service Map
//        Map<String, Object> budgetMap = budgetService.getBudgetByUserId(currentUserId);
//        
//        System.out.println(budgetMap);
//        
//        if (budgetMap != null && "1".equals(String.valueOf(budgetMap.get("Status")))) {
//            com.minku.expensetracker.entity.Budget budgetObj = (com.minku.expensetracker.entity.Budget) budgetMap.get("Data");
//            if (budgetObj != null) {
//            	model.addAttribute("budgetLimit",  budgetLimit = budgetObj.getMonthlyLimit()); 
//            	model.addAttribute("totalSpent",  totalSpent = budgetObj.getSpentAmount());   
//            }
//        }
//
//        // Calculations metrics setup
//        double remainingBudget = budgetLimit - totalSpent;
//        long consumptionPercentage = 0;
//        if (budgetLimit > 0) {
//            consumptionPercentage = Math.round((totalSpent / budgetLimit) * 100);
//        }
//
//        // Fetching transactions lists stream logs
//        Map<String, Object> expenseMap = expenseService.getAllExpenses();
//        if (expenseMap != null && "1".equals(String.valueOf(expenseMap.get("Status")))) {
//            model.addAttribute("recentExpenses", expenseMap.get("Data"));
//        }
//
//        // Passing success banners variables dynamically onto thymeleaf
//        if (successMessage != null) {
//            model.addAttribute("message", successMessage);
//        }
//
//        model.addAttribute("budgetLimit", budgetLimit);
//        model.addAttribute("totalSpent", totalSpent);
//        model.addAttribute("remainingBudget", remainingBudget);
//        model.addAttribute("consumptionPercentage", consumptionPercentage > 100 ? 100 : consumptionPercentage);
//
//        return "dashboard";
//    }
//
//    // ==========================================
//    // 💸 100% WORKING FIXED EXPENSE ENTRY HANDLING
//    // ==========================================
//
//    @GetMapping("/expenses/add-view")
//    public String renderExpenseForm(HttpSession session, Model model) {
//        if (session.getAttribute("userId") == null) {
//            return "redirect:/login";
//        }
//        return "add-expense"; // Render form without model binder mismatch crash issues
//    }
//
//    @PostMapping("/expenses/save")
//    public String saveExpenseFromUi(@RequestParam String title,
//                                     @RequestParam Double amount,
//                                     @RequestParam String category,
//                                     @RequestParam String expenseDate,
//                                     HttpSession session, Model model) {
//        
//        Long currentUserId = (Long) session.getAttribute("userId");
//        if (currentUserId == null) {
//            return "redirect:/login";
//        }
//
//        try {
//            // Mapping html plain request elements onto the DTO layer object properties securely
//            ExpenseRequestDto requestDto = new ExpenseRequestDto();
//            requestDto.setTitle(title);
//            requestDto.setAmount(amount);
//            requestDto.setCategory(category);
//            requestDto.setExpenseDate(java.time.LocalDate.parse(expenseDate));
//            requestDto.setUserId(currentUserId);
//
//            // Forward logic processing matrix onto expense service check rules
//            Map<String, Object> serviceResult = expenseService.addExpense(requestDto);
//
//            // Budget limits cap check exception analyzer trap
//            if (serviceResult != null && "0".equals(String.valueOf(serviceResult.get("Status")))) {
//                String errorMsg = serviceResult.get("Error") != null ? String.valueOf(serviceResult.get("Error")) : String.valueOf(serviceResult.get("Data"));
//                model.addAttribute("error", errorMsg);
//                return "add-expense";
//            }
//
//            // SUCCESSFUL PASS: Single Page Redirect mapping with direct visual query parameter message
//            return "redirect:/dashboard?message=Expense Added Successfully";
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Data validation parse crash error: " + e.getMessage());
//            return "add-expense";
//        }
//    }
//}
