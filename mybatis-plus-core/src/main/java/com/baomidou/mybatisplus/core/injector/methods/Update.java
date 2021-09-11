/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.core.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 根据 whereEntity 条件，更新记录
 *
 * @author hubin
 * @since 2018-04-06
 */
public class Update extends AbstractMethod {

    /**
     * <pre>
     * 包含逻辑删除字段：
     *
     * <script>
     * UPDATE {tableName}
     *     <set>
     *         <if test="et != null">
     *             column=#{et.propertyName,jdbcType={jdbcTypeName},javaType={javaTypeName},typeHandler={typeHandlerName},numericScale={numericScaleName}},
     *         </if>
     *         <if test="ew != null and ew.sqlSet != null">${ew.sqlSet}</if>
     *     </set>
     *     <where>
     *         <choose>
     *             <when test="ew != null">
     *                 <if test="ew.entity != null">
     *                     <if test="ew.entity.keyProperty != null">keyColumn=#{{ew.entity.keyProperty}}</if>
     *                     <if test="ew.entity.propertyName != null and ew.entity.propertyName != ''">
     *                         AND columnName=#{ew.entity.propertyName,jdbcType={jdbcTypeName},javaType={javaTypeName},typeHandler={typeHandlerName},numericScale={numericScaleName}}
     *                     </if>
     *                 </if>
     *                 AND {logicDeleteColumn}={logicNotDeleteValue}
     *                 <if test="ew.sqlSegment != null and ew.sqlSegment != '' and ew.nonEmptyOfNormal">
     *                     AND ${ew.sqlSegment}
     *                 </if>
     *                 <if test="ew.sqlSegment != null and ew.sqlSegment != null and ew.emptyOfNormal">
     *                     ${ew.sqlSegment}
     *                 </if>
     *             </when>
     *             <otherwise>{logicDeleteColumn}={logicNotDeleteValue}</otherwise>
     *         </choose>
     *     </where>
     *     <choose>
     *         <when test="ew != null and ew.sqlComment != null">
     *             ${ew.sqlComment}
     *         </when>
     *         <otherwise></otherwise>
     *     </choose>
     * </script>
     *
     * </pre>
     *
     * <pre>
     * 不包含逻辑删除字段：
     *
     * <script>
     * UPDATE {tableName}
     *     <set>
     *         <if test="et != null">
     *             column=#{et.propertyName,jdbcType={jdbcTypeName},javaType={javaTypeName},typeHandler={typeHandlerName},numericScale={numericScaleName}},
     *         </if>
     *         <if test="ew != null and ew.sqlSet != null">${ew.sqlSet}</if>
     *     </set>
     *     <if test="ew != null">
     *         <where>
     *             <if test="ew.entity != null">
     *                 <if test="ew.entity.keyProperty != null">keyColumn=#{{ew.entity.keyProperty}}</if>
     *                 <if test="ew.entity.propertyName != null and ew.entity.propertyName != ''">
     *                     AND columnName=#{ew.entity.propertyName,jdbcType={jdbcTypeName},javaType={javaTypeName},typeHandler={typeHandlerName},numericScale={numericScaleName}}
     *                 </if>
     *             </if>
     *             <if test="ew.sqlSegment != null and ew.sqlSegment != '' and ew.nonEmptyOfWhere">
     *                 <if test="ew.nonEmptyOfEntity and ew.nonEmptyOfNormal"> AND</if> ${ew.sqlSegment}
     *             </if>
     *         </where>
     *         <if test="ew.sqlSegment != null and ew.sqlSegment != '' and ew.emptyOfWhere">
     *             ${ew.sqlSegment}
     *         </if>
     *     </if>
     *     <choose>
     *         <when test="ew != null and ew.sqlComment != null">
     *             ${ew.sqlComment}
     *         </when>
     *         <otherwise></otherwise>
     *     </choose>
     *
     * </script>
     * </pre>
     *
     * @param mapperClass mapper 接口
     * @param modelClass  mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE;
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
            sqlSet(true, true, tableInfo, true, ENTITY, ENTITY_DOT),
            sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, getMethod(sqlMethod), sqlSource);
    }
}
