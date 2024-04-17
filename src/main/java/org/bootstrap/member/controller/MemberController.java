package org.bootstrap.member.controller;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

}
