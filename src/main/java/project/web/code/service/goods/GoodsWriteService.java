package project.web.code.service.goods;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.web.code.dto.AuthInfoDTO;
import project.web.code.dto.GoodsCommand;
import project.web.code.dto.GoodsDTO;
import project.web.code.mapper.employee.EmployeeMapper;
import project.web.code.mapper.goods.GoodsMapper;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoodsWriteService {

   private final EmployeeMapper employeeMapper;
   private final GoodsMapper goodsMapper;

    public void execute(GoodsCommand goodsCommand, HttpSession session) {

        GoodsDTO.Response dto = new GoodsDTO.Response();
        dto.setGoodsContent(goodsCommand.getGoodsContent());
        dto.setGoodsName(goodsCommand.getGoodsName());
        dto.setGoodsNum(goodsCommand.getGoodsNum());
        dto.setGoodsPrice(goodsCommand.getGoodsPrice());
        dto.setDeliveryCost(goodsCommand.getDeliveryCost());
        AuthInfoDTO auth = (AuthInfoDTO)session.getAttribute("auth");
        String empId = auth.getUserId();
        String empNum = employeeMapper.getEmpNum(empId);
        dto.setEmpNum(empNum);

        URL resource = getClass().getClassLoader().getResource("static/upload");

        String fileDir = resource.getFile();
        /// 대문이미지
        MultipartFile mf = goodsCommand.getGoodsMainStore(); //command에서 이미지객체를 가지고 옵니다.
        String originalFile = mf.getOriginalFilename(); // 파일객체에서 upload할 때 사용한 파일명을 가지고 옵니다.
        String extension = originalFile.substring(originalFile.lastIndexOf(".")); // 파일명에서 확장자만 추출
        String storeName = UUID.randomUUID().toString().replace("-", ""); // 임의의 이름을 부여하기 위해서 사용
        String storeFileName = storeName + extension; //새로운 파일명을 만들어 줍니다.
        // 새로운 파일명으로 파일을 저장합니다.
        // static/upload에 새로운 파일명으로 파일을 저장하겠습니다.
        File file = new File(fileDir + "/"+ storeFileName);
        try {
            mf.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //파일을 저장했으니 dto에 파일명을 저장하겠습니다.
        dto.setGoodsMainStore(storeFileName); // static/upload에 저장된 이름
        dto.setGoodsMainStoreImg(originalFile); // upload할 때 사용된 이름
        // multiple로 가져온 파일 처리 필수 항목이 아니므로 파일이 있는 지 먼저 확인합니다.
        if(!goodsCommand.getGoodsImages()[0].getOriginalFilename().isEmpty()) {
            //다음은 위 코드와 같으만 여러개의 파일명을 저장할 작업을 해 줍니다.
            String originalTotal = "";
            String storeTotal = "";
            //파일이 여러개 이므로 반복하여 위 코드를 사용합니다.
            for(MultipartFile mtf : goodsCommand.getGoodsImages()) {
                originalFile = mtf.getOriginalFilename();
                extension = originalFile.substring(originalFile.lastIndexOf("."));
                storeName = UUID.randomUUID().toString().replace("-", "");
                storeFileName = storeName + extension;

                file = new File(fileDir + "/"+ storeFileName);
                try {
                    mtf.transferTo(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //파일명을 하나의 문자열로 만드렁 줍니다.
                originalTotal += originalFile + "-";
                storeTotal += storeFileName + "-";
            }
            //이제 dto에 저장합니다.
            dto.setGoodsImages(storeTotal);
            dto.setGoodsImagesImg(originalTotal); // 여기까지가 파일저장하는 방법입니다.
        }
        // 주의 할 점 src에 저장하는 것이 마니고 bin에 저장됩니다.프로젝트에서는 보이지 않습니다.
        goodsMapper.goodsInsert(dto);
    }
}
