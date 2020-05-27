package com.cao.xtransaction.server;


import com.cao.xtransaction.IService;
import com.cao.xtransaction.annotation.XTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService implements IService {

    @Autowired
    private DemoDao demoDao;

    //@XTransactional(isEnd = true)
    @Transactional
    public void test() {
        demoDao.insert("server2");
    }
}
