package com.easyone.travelance.domain.account.controller;

import com.easyone.travelance.domain.account.dto.BalanceRequestDto;
import com.easyone.travelance.domain.account.dto.OneCheckRequestDto;
import com.easyone.travelance.domain.account.dto.OneRequestDto;
import com.easyone.travelance.domain.account.dto.SelectedAccountRequestDto;
import com.easyone.travelance.domain.account.service.AccountService;
import com.easyone.travelance.domain.member.entity.MainAccount;
import com.easyone.travelance.domain.member.entity.Member;
import com.easyone.travelance.domain.member.service.MemberService;
import com.easyone.travelance.global.memberInfo.MemberInfo;
import com.easyone.travelance.global.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account")
@Slf4j
public class AccountController {

    private final MemberService memberService;
    private final AccountService accountService;

    @Operation(summary = "1원이체 요청", description = "요청 시 사용자의 계좌로 1원 및 랜덤으로 생성된 4자리 숫자를 보냅니다." +
            "name : 사용자이름, bankname: 은행명, account : 계좌번호")
    @PostMapping(value = "/1request")
    public Mono<ResponseEntity<Object>> oneTransferMoney(@MemberInfo MemberInfoDto memberInfoDto, @RequestBody OneRequestDto oneRequestDto){
        String name = oneRequestDto.getName();
        String bankName = oneRequestDto.getBankName();
        String account = oneRequestDto.getAccount();

        return accountService.oneTransferMoney(name, bankName, account)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 만약 데이터가 없을 경우의 처리
    }

    @Operation(summary = "1원이체 확인", description = "1원 이체시 받은 난수와 비교 후 privateId를 반환합니다." +
            "name : 사용자이름, bankname: 은행명, account : 계좌번호, verifyCode : 난수")
    @PostMapping(value = "/1response")
    public Mono<ResponseEntity<Object>> oneCheckMoney(@MemberInfo MemberInfoDto memberInfoDto, @RequestBody OneCheckRequestDto oneCheckRequestDto){
        String name = oneCheckRequestDto.getName();
        String bankName = oneCheckRequestDto.getBankName();
        String account = oneCheckRequestDto.getAccount();
        String verifyCode = oneCheckRequestDto.getVerifyCode();

        return accountService.oneCheckMoney(name, bankName, account,verifyCode)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 만약 데이터가 없을 경우의 처리
    }

    @Operation(summary = "주 계좌 잔액 조회", description = "주 계좌의 잔액을 조회합니다.")
    @PostMapping(value = "search/balance")
    public Mono<ResponseEntity<Object>> searchBalance(@MemberInfo MemberInfoDto memberInfoDto, @RequestBody BalanceRequestDto balanceRequestDto){
        // 로그인한 사람
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        return accountService.searchBalance(member, balanceRequestDto)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 만약 데이터가 없을 경우의 처리
    }


    @Operation(summary = "전체계좌 조회", description = "나의 모든 계좌를 조회하는 메서드 입니다")
    @PostMapping("/allaccount")
    public Mono<ResponseEntity<List<Object>>> allAccount(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        return accountService.allAccount(member.getPrivateId())
                .collectList() // Flux를 Mono<List<Object>>로 변환합니다.
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 만약 데이터가 없을 경우의 처리
    }

    @Operation(summary = "계좌 목록 DB 저장", description = " 내가 선택한 계좌를 DB에 저장하는 메서드 입니다\n\n" + "[\n\n" +
            "    {\n" + "      \"account\": \"7753621811018015\",\n" + "      \"bankName\": \"SC제일은행\",\n" + "      \"accountUrl\": \"img/bank/001_SC제일은행\"\n" + "    },    \n" + "    {\n" + "      \"account\": \"7753621811018015\",\n" + "      \"bankName\": \"SC제일은행\",\n" + "      \"accountUrl\": \"img/bank/001_SC제일은행\"\n" + "    },    \n\n"+ "]\n\n" + "이런 형식으로 넣으시면 됩니다.")
    @PostMapping("/selectedaccount")
    public ResponseEntity<String> SaveAccount(@RequestBody List<SelectedAccountRequestDto> selectedAccountRequestDtoList, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        MainAccount mainAccount = member.getMainAccount();
        log.warn("mainAccount : " + mainAccount);
        log.warn("Received JSON data: " + selectedAccountRequestDtoList);
        for (SelectedAccountRequestDto selectedAccountRequestDto : selectedAccountRequestDtoList) {
            accountService.SaveAccount(mainAccount, selectedAccountRequestDto);
        }

        return ResponseEntity.ok("success");
    }
}