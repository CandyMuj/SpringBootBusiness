package com.cc.api.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellUtil;
import cn.hutool.poi.excel.style.StyleUtil;
import com.cc.api.exception.CandyException;
import com.cc.api.pojo.sys.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @Description excel工具封装
 * @Author CandyMuj
 * @Date 2023/6/2 14:28
 * @Version 1.0
 */
@Slf4j
public class ExcelUtilc {

    /**
     * Excel导出
     * <p>
     * 糊涂工具实现
     * <p>
     * 相比easyexcel，他不需要任何注解，任何内部类，只需要简单的pojo vo即可 什么都不需要，只要数据，只注重数据
     *
     * @param bigWriter 是否使用 Excel大数据生成：对于大量数据输出，采用ExcelWriter容易引起内存溢出，因此有了BigExcelWriter，使用方法与ExcelWriter完全一致。  true：使用 false：不使用
     */
    public static <T> void getExcel(
            boolean bigWriter,
            HttpServletResponse response,
            String fileName,
            List<T> datas,
            List<String> titles,
            Function<T, List<String>> generateRow
    ) {
        Assert.isTrue(StrUtil.isNotBlank(fileName), "文件名不可为空");
        fileName = (fileName.endsWith(".xlsx") ? fileName : StrUtil.format("{}.xlsx", fileName));

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 写出文件到流
            ExcelWriter writer = bigWriter ? ExcelUtil.getBigWriter() : ExcelUtil.getWriter(true);
            // 设置头部样式
            CellStyle headStyle = writer.getHeadCellStyle();
            Font font_head = writer.createFont();
            font_head.setFontName("宋体");
            font_head.setBold(true);
            font_head.setFontHeightInPoints((short) 14);
            headStyle.setFont(font_head);
            headStyle.setWrapText(true);
            // 写出头部
            writer.writeHeadRow(titles);

            // 设置非头部的单元格样式 默认样式
            CellStyle cellStyle = writer.createCellStyle();
            StyleUtil.setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            Font font_cell = writer.createFont();
            font_cell.setFontName("宋体");
            font_cell.setFontHeightInPoints((short) 11);
            cellStyle.setFont(font_cell);
            // 写出非头部，并自定义部分样式
            int ri = 1;
            for (T t : datas) {
                // 获取当前行的数据
                List<String> rowData = generateRow.apply(t);
                // 创建行
                Row row = writer.getSheet().createRow(ri++);
                row.setHeightInPoints(24);

                // 创建单元格
                int ci = 0;
                for (String value : rowData) {
                    Cell cell = row.createCell(ci++);
                    cell.setCellStyle(cellStyle);
                    CellUtil.setCellValue(cell, value);
                }
            }

            // 设置所有行的默认高度
            writer.setDefaultRowHeight(24);
            // 设置头部行的高度
            writer.setRowHeight(0, 66);

            // 写出数据
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")));
            writer.flush(outputStream);
            writer.close();
        } catch (Exception e) {
            log.error("excel hutool 生成异常", e);
            throw new CandyException(e.getMessage());
        }
    }

    public static <T> void getExcel(
            HttpServletResponse response,
            String fileName,
            List<T> datas,
            List<String> titles,
            Function<T, List<String>> generateRow
    ) {
        getExcel(true, response, fileName, datas, titles, generateRow);
    }


    /**
     * 调用示例
     */
    private void simple() {
        // 糊涂工具的调用示例
        List<User> hutoolDatas = new ArrayList<>();
        ExcelUtilc.getExcel(true, null, "这是文件名", hutoolDatas,
                CollUtil.newArrayList("手机号", "昵称"),
                (data) -> CollUtil.newArrayList(
                        data.getPhone(),
                        data.getUserName()
                )
        );
    }
}
