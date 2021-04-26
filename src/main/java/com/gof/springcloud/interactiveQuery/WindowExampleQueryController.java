package com.gof.springcloud.interactiveQuery;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WindowExampleQueryController {

//	  @Autowired
//	  private InteractiveQueryService interactiveQueryService;
//
//	  @GetMapping("GetByKey")
//	  @ApiOperation(value = "Get Record by Key")
//		public ResultVo<String> sendSumMsg(String key) {
//			final ReadOnlyKeyValueStore<String, String> queryableStore = interactiveQueryService
//					.getQueryableStore("windowStore", QueryableStoreTypes.keyValueStore());
//			ResultVo<String> resultVo = new ResultVo<String>(true);
//			resultVo.setObj(queryableStore.get(key));
//			return resultVo;
//		}
//
}
