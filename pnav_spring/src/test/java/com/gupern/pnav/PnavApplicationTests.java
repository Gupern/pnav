package com.gupern.pnav;

import com.gupern.pnav.common.util.CryptoUtil;
import com.gupern.pnav.wechat.bean.PojoConstants;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CyclicBarrier;

import static jdk.nashorn.internal.objects.Global.print;

@SpringBootTest
class pnavApplicationTests {
	@Test
	void contextLoads() {
		System.out.println(PojoConstants.APPSECRET);

	}

}
