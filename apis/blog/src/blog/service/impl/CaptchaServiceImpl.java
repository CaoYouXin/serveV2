package blog.service.impl;

import beans.BeanManager;
import blog.data.EICaptcha;
import blog.repository.ICaptchaRepo;
import blog.service.ICaptchaService;
import blog.service.exp.CaptchaException;
import blog.service.exp.TableNotCreateException;
import util.DateUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CaptchaServiceImpl implements ICaptchaService {

    private ICaptchaRepo captchaRepo = BeanManager.getInstance().getRepository(ICaptchaRepo.class);

    private final char[] CHAR_CODE = { '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    private static Random RANDOM = new Random();
    private static Font FONT = new Font("Monospaced", Font.PLAIN, 18);

    @Override
    public void before() {
        if (!this.captchaRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("captcha");
        }
    }

    @Override
    public byte[] generateCaptcha(String token, int width, int height) throws CaptchaException {
        String code = this.generateCode(4, true);

        EICaptcha eiCaptcha = BeanManager.getInstance().createBean(EICaptcha.class);
        eiCaptcha.setCaptchaCode(code);
        eiCaptcha.setCaptchaToken(token);
        eiCaptcha.setCaptchaDeadTime(DateUtil.calc(new Date(), Calendar.MINUTE, 30));

        if (!this.captchaRepo.save(eiCaptcha)) {
            throw new CaptchaException("无法保存验证码.");
        }

        return this.generateImage(code, width, height, 10);
    }

    private byte[] generateImage(String str, int width, int height, int line) {
        char[] code = str.toCharArray();

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(this.getRandColor(110, 133));
        // 绘制干扰线
        for (int i = 0; i < line; i++) {
            this.drawLine(g, width, height);
        }
        // 绘制随机字符
        for (int i = 0; i < code.length; i++) {
            this.drawChar(g, width / code.length * i, RANDOM.nextInt(height / 3)
                    + height / 3, code[i]);
        }
        g.dispose();

        return this.convertImageToByteArray(image);
    }

    private byte[] convertImageToByteArray(BufferedImage image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", bos);
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private void drawChar(Graphics g, int x, int y, char code) {
        g.setFont(FONT);
        g.setColor(getRandColor(10, 200));
        g.translate(RANDOM.nextInt(3), RANDOM.nextInt(3));
        g.drawString(code + "", x, y);
    }

    private void drawLine(Graphics g, int width, int height) {
        int x = RANDOM.nextInt(width);
        int y = RANDOM.nextInt(height);
        int xl = RANDOM.nextInt(width / 2);
        int yl = RANDOM.nextInt(height / 2);
        g.drawLine(x, y, x + xl, y + yl);
    }

    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + RANDOM.nextInt(bc - fc - 16);
        int g = fc + RANDOM.nextInt(bc - fc - 14);
        int b = fc + RANDOM.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    private String generateCode(int len, boolean isCanRepeat) throws CaptchaException {
        char[] code = Arrays.copyOf(CHAR_CODE, CHAR_CODE.length);
        int n = code.length;

        if (len > n && !isCanRepeat) {
            throw new CaptchaException("需要的验证码太长.");
        }

        char[] result = new char[len];
        if (isCanRepeat) {
            for (int i = 0; i < result.length; i++) {
                int r = (int) (Math.random() * n);
                result[i] = code[r];
            }
        } else {
            for (int i = 0; i < result.length; i++) {
                int r = (int) (Math.random() * n);
                result[i] = code[r];
                code[r] = code[n - 1];
                n--;
            }
        }
        return new String(result);
    }
}
