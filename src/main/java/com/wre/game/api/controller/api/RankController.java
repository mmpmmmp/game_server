package com.wre.game.api.controller.api;

import com.wre.game.api.constants.ApiHeader;
import com.wre.game.api.constants.ApiResource;
import com.wre.game.api.data.dto.UserRanksDTO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 排行榜
 * @author zs
 * @date 2020-03-03 15:46:10
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_DATA)
public class RankController {
	public HashMap<String, UserRanksDTO> rankMap = new HashMap<>();

	/**
	 * 获取用户排行
	 * @return
	 */
	@RequestMapping(value = "/ranknew", method = { RequestMethod.GET })
	public UserRanksDTO uploadGameData(@RequestHeader(value = ApiHeader.APP_ID) String appId) {
		return rankMap.get(appId);
	}

}
