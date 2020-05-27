package com.cao.xtransaction.server;


import com.cao.xtransaction.annotation.XTransactional;
import com.cao.xtransaction.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    //@XTransactional(isStart = true)
    @Transactional
    public void test() {
        demoDao.insert("server1");
        HttpClient.get("http://localhost:8082/server2/test");
        int i = 100/0;
    }
}
