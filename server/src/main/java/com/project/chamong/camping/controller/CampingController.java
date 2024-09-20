package com.project.chamong.camping.controller;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.dto.ContentResponseDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.mapper.CampingApiMapper;
import com.project.chamong.camping.service.CampingApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Writer : 최준영
 * Date   : 2023-03-08
 * Description : Main Controller
 * <p>
 * keywordId(키워드 클릭시 단발적인 이동) = 1(오션뷰), 2(피톤치드), 3(애견동반), 4(운동), 5(물놀이 시간), 6(단풍), 7(봄꽃여행), 8(일몰명소), 9(인기)
 * areaId = 1(서울), 2(대구/경북), 3(강원), 4(경기/인천), 5(광주/전라), 6(대전/충청), 7(제주), 8(부산/경남)
 * themeId : 1(화장실), 2(산), 3(강), 4(섬), 5(숲), 6(호수), 7(해변), 8(와이파이), 9(전기), 10(운동시설), 11(물놀이), 12(마트), 13(편의점), 14(체험활동),
 * 15(낚시), 16(반려동물), 17(운영중)
 */

@RestController
@RequestMapping("/main")
public class CampingController {

    CampingApiService campingApiService;
    CampingApiMapper mapper;
    RestTemplate restTemplate;
    String servicekey = "9Oo%2BZ6xZdxKcsnKFsJI6eqFfkst%2BLrlJL5fuDTWxlA88jrbjhaaIsOUcgSRGRwcm%2FO%2FpZE%2FXSwpiBwxkBqXf%2FA%3D%3D";
    int numOfRows = 3990;
    int pageNo = 1;
    String mobileOs = "ETC";
    String mobileApp = "Chamong";

    public CampingController(CampingApiService campingApiService,
                             CampingApiMapper mapper, RestTemplate restTemplate) {
        this.campingApiService = campingApiService;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    // 고캠핑 API 저장
    @PostMapping("/content")
    public String postCampingApi() {
        URI uri = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("apis.data.go.kr")
                .path("/B551011/GoCamping/basedList")
                .queryParam("serviceKey", servicekey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .encode()
                .build(true)
                .toUri();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String result = restTemplate.getForObject(uri, String.class);

        JSONObject jsonObject = XML.toJSONObject(result);
        JSONArray itemsArr = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        double mapX;
        double mapY;

        for (int i = 0; i < itemsArr.length(); i++) {
            JSONObject item = itemsArr.getJSONObject(i);
            String facltNm = String.valueOf(item.get("facltNm"));
            String lineIntro = String.valueOf(item.get("lineIntro"));
            String intro = String.valueOf(item.get("intro"));
            String themaEnvrnCl = String.valueOf(item.get("themaEnvrnCl"));
            if (item.get("mapX") instanceof String) {
                mapX = 0.0;
                mapY = 0.0;
            } else {
                BigDecimal bdMapX = (BigDecimal) item.get("mapX");
                BigDecimal bdMapY = (BigDecimal) item.get("mapY");
                mapX = bdMapX.doubleValue();
                mapY = bdMapY.doubleValue();
            }
            String addr1 = String.valueOf(item.get("addr1"));
            String tel = String.valueOf(item.get("tel"));
            String homepage = String.valueOf(item.get("homepage"));
            String resveCl = String.valueOf(item.get("resveCl"));
            String doNm = String.valueOf(item.get("doNm"));
            String manageSttus = String.valueOf(item.get("manageSttus"));
            String induty = String.valueOf(item.get("induty"));
            String firstImageUrl = String.valueOf(item.get("firstImageUrl"));
            String createdtime = String.valueOf(item.get("createdtime"));
            String modifiedtime = String.valueOf(item.get("modifiedtime"));
            String featureNm = String.valueOf(item.get("featureNm"));
            String brazierCl = String.valueOf(item.get("brazierCl"));
            String glampInnerFclty = String.valueOf(item.get("glampInnerFclty"));
            String caravInnerFclty = String.valueOf(item.get("caravInnerFclty"));
            String sbrsCl = String.valueOf(item.get("sbrsCl"));
            String animalCmgCl = String.valueOf(item.get("animalCmgCl"));
            String exprnProgrmAt = String.valueOf(item.get("exprnProgrmAt"));
            String exprnProgrm = String.valueOf(item.get("exprnProgrm"));
            String posblFcltyCl = String.valueOf(item.get("posblFcltyCl"));
            String lctCl = String.valueOf(item.get("lctCl"));

            CampingApiDto.Post CampingApiDto = new CampingApiDto.Post(
                    facltNm,
                    lineIntro,
                    intro,
                    themaEnvrnCl,
                    mapX,
                    mapY,
                    addr1,
                    tel,
                    homepage,
                    resveCl,
                    doNm,
                    manageSttus,
                    induty,
                    firstImageUrl,
                    createdtime,
                    modifiedtime,
                    featureNm,
                    brazierCl,
                    glampInnerFclty,
                    caravInnerFclty,
                    sbrsCl,
                    animalCmgCl,
                    exprnProgrmAt,
                    exprnProgrm,
                    posblFcltyCl,
                    lctCl
            );
            Content content = new Content(CampingApiDto);
            campingApiService.postCampingApi(content);
        }
        return "suceess";
    }

    // 캠핑장 키워드 검색
    @GetMapping("/search/keyword/{keyword-id}")
    public ResponseEntity searchKeyword(@RequestParam(defaultValue = "0") Long lastContentId,
                                        @PathVariable("keyword-id") @Positive int keywordId,
                                        @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto) {
        List<ContentResponseDto> contentResponseDtos = campingApiService.findKeyword(lastContentId, keywordId, authorizedMemberDto);
        
        return new ResponseEntity<>(contentResponseDtos, HttpStatus.OK);
    }

    // 캠핑장 검색
    @GetMapping("/search/{thema-id}/{place-id}")
    public ResponseEntity searchCamping(@PathVariable("thema-id") @Positive int themaId,
                                        @PathVariable("place-id") @Positive int placeId,
                                        @RequestParam(defaultValue = "0") Long lastContentId,
                                        @RequestParam(required = false) String keyword,
                                        @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto) {
        List<ContentResponseDto> contentResponseDtos =
          campingApiService.findCamping(lastContentId, keyword, themaId, placeId, authorizedMemberDto);
        
        return new ResponseEntity<>(contentResponseDtos, HttpStatus.OK);
    }

    // 캠핑장 메인 페이지
    @GetMapping
    public ResponseEntity<?> getContents(@RequestParam(defaultValue = "0") Long lastContentId,
                                         @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto) {
        List<ContentResponseDto> contents = campingApiService.findContents(lastContentId, authorizedMemberDto);
        
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }

    // 캠핑장 상세 페이지
    @GetMapping("/{content-id}")
    public ResponseEntity getContent(@PathVariable("content-id") @Positive long contentId,
                                     @AuthenticationPrincipal @Nullable AuthorizedMemberDto authorizedMemberDto) {
        ContentResponseDto content = campingApiService.findContentResponse(contentId, authorizedMemberDto);
        
        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}