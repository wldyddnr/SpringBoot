package com.example.demo.controller;

import com.example.demo.domain.Member;
import com.example.demo.service.EmailService;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @Autowired
    public MemberController(MemberService memberService, EmailService emailService) {
        this.memberService = memberService;
        this.emailService = emailService;
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "LoginForm";
    }


    @PostMapping("/login")
    public String login(Model model, Member member) {
        Member loginMember = memberService.login(member.getId());
        if (loginMember == null) {
            return "LoginFail";
        } else if (!loginMember.getPassword().equals(member.getPassword())) {
            return "LoginFail";
        }
        model.addAttribute("member", loginMember);
        System.out.println(loginMember.toString());
        return "MemberInfoForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "JoinForm";
    }

    @PostMapping("/join")
    public String join(Member member) {
        memberService.join(member);
        return "JoinSuccess";
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "FindPasswordForm";
    }

    @GetMapping("/findId")
    public String findId() {
        return "FindIdForm";
    }

    @GetMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam String id) {
        System.out.println(id);
        return memberService.checkId(id);
    }

    @GetMapping("/logout")
    public String logout(SessionStatus session) {
        session.setComplete();
        return "LoginForm";
    }

    @GetMapping("/memberInfo")
    public String memberInfo() {
        return "MemberInfoForm";
    }

    @PostMapping("/updateInfo")
    public String updateInfo(Member member, Model model) {
        memberService.updateInfo(member);
        model.addAttribute("member", member);
        return "UpdateSuccess";
    }

    @GetMapping("/updatePasswordForm")
    public String updatePasswordForm() {
        return "UpdatePassword";
    }

    @PostMapping("updatePassword")
    public String updatePassword(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        memberService.updateInfo(member);
        model.addAttribute("member", member);
        return "UpdateSuccess";
    }

    @GetMapping("deleteMember")
    public String deleteMember(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        memberService.deleteMember(member);
        return "DeleteSuccess";
    }

    @PostMapping("findPassword")
    public String findPassword(String email, String id) throws MessagingException {
        Member member = memberService.findPassword(id);
        if (member != null) {
            StringBuffer emailcontent = new StringBuffer();
            emailcontent.append(member.getPassword());
            emailService.sendMail(email, "???????????? ??????", emailcontent.toString());
            return "FindSuccess";
        }
        return "FindFail";
    }

    @PostMapping("findId")
    public String findId(String email, String name) throws MessagingException {
        Member member = memberService.findId(email, name);
        System.out.println(member.toString());
        if (member != null) {
            StringBuffer emailcontent = new StringBuffer();
            emailcontent.append(member.getId());
            emailService.sendMail(email, "????????? ??????", emailcontent.toString());
            return "FindSuccess";
        }
        return "FindFail";

    }
}
