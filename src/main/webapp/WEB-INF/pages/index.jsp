<%-- 
    Document   : index
    Created on : Jul 8, 2023, 1:08:00 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="container-fluid p-4">
    <div class="row">
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body" style="padding: 1.25rem;">
                    <div class="row align-items-center">
                        <div class="col" style="margin-right: 0.5rem !important">
                            <div class="font-weight-bold text-primary text-uppercase mb-1" style="font-size: 0.8rem">
                                Total users
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${totalUsers}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-user fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body" style="padding: 1.25rem;">
                    <div class="row align-items-center">
                        <div class="col" style="margin-right: 0.5rem !important">
                            <div class="font-weight-bold text-success text-uppercase mb-1" style="font-size: 0.8rem">
                                Total posts
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${totalPosts}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-info shadow h-100 py-2">
                <div class="card-body" style="padding: 1.25rem;">
                    <div class="row align-items-center">
                        <div class="col" style="margin-right: 0.5rem !important">
                            <div class="font-weight-bold text-info text-uppercase mb-1" style="font-size: 0.8rem">Total
                                alumni
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${totalAlumni}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-user fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-warning shadow h-100 py-2">
                <div class="card-body" style="padding: 1.25rem;">
                    <div class="row align-items-center">
                        <div class="col" style="margin-right: 0.5rem !important">
                            <div class="font-weight-bold text-warning text-uppercase mb-1" style="font-size: 0.8rem">
                                Total groups
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${totalGroups}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-users fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div>
        <form method="get" action="<c:url value="/admin"/>">
            <div class="mt-3 mb-3 row">
                <div class="col-xl-2 col-md-2 col-sm-2">
                    <select id="selectMonth" name="month">
                        <script>
                            function generateMonth() {
                                var options = '';
                                options += '<option value="" selected >Month</option>'
                                for (let i = 1; i <= 12; i++) {
                                    options += '<option value="' + i + '">' + "Tháng " + i + ' </option>';
                                }
                                return options;
                            }

                            var selectMonthElement = document.getElementById("selectMonth");
                            var monthOptions = generateMonth();
                            selectMonthElement.innerHTML = monthOptions;
                        </script>
                    </select>
                </div>
                <div class="col-xl-2 col-md-2 col-sm-2">
                    <select id="selectYear" name="year">
                        <script>
                            function getCurrentYear() {
                                return new Date().getFullYear();
                            }

                            function generateYearOptions(startYear, endYear) {
                                var options = '';
                                options += '<option value="" selected >Year</option>'
                                for (var year = startYear; year <= endYear; year++) {
                                    options += '<option value="' + year + '">' + year + '</option>';
                                }
                                return options;
                            }

                            var currentYear = getCurrentYear();
                            var selectYearElement = document.getElementById("selectYear");
                            var yearOptions = generateYearOptions(2010, currentYear);
                            selectYearElement.innerHTML = yearOptions;
                        </script>
                    </select>
                </div>
                <div class="col-xl-2 col-md-2 col-sm-2">
                    <select id="selectQuarter" name="quarter">
                        <option value="" selected>Quarter</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                    </select>
                </div>
                <div class="col-xl-2 col-md-2 col-sm-2">
                    <input type="submit" class="btn btn-primary" value="Report">
                </div>
            </div>
        </form>
    </div>

    <div class="row">
        <div class="col-xl-8 col-lg-7">
            <div class="card shadow mb-4">
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">Posts statistic</h6>
                </div>
                <div class="card-body">
                    <div class="chart-area">
                        <div id="chartContainer1" style="height: 100%; width: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-4 col-lg-5">
            <div class="card shadow mb-4">
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">Users type</h6>
                </div>
                <div class="card-body">
                    <div class="chart-pie pt-4 pb-2">
                        <div id="chartContainer" style="height: 100%; width: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    window.onload = function () {

        var dps = [[]];
        var chart = new CanvasJS.Chart("chartContainer", {
            theme: "light2", // "light1", "dark1", "dark2"
            exportEnabled: true,
            animationEnabled: true,
            title: {
                text: "Users type"
            },
            data: [{
                type: "pie",
                showInLegend: "true",
                legendText: "{label}",
                yValueFormatString: "#,###\"%\"",
                indexLabelFontSize: 16,
                indexLabel: "{label} - {y}",
                dataPoints: dps[0]
            }]
        });

        var yValue;
        var label;

        <c:forEach items="${userResponses}" var="dataPoint">
        yValue = parseFloat("${dataPoint.percent}");
        label = "${dataPoint.name}";
        dps[0].push({
            label: label,
            y: yValue,
        });
        </c:forEach>

        chart.render();

        var dps2 = [[]];
        var chart2 = new CanvasJS.Chart("chartContainer1", {
            theme: "light2", // "light1", "dark1", "dark2"
            animationEnabled: true,
            title: {
                text: "Posts statistic"
            },
            axisX: {
                valueFormatString: "####"
            },
            axisY: {
                title: "Quantity"
            },
            data: [{
                type: "spline",
                xValueFormatString: "####",
                yValueFormatString: "#,##0.0 posts",
                dataPoints: dps2[0]
            }]
        });

        var xValue2;
        var yValue2;

        <c:forEach items="${statsPostResponse}" var="dataPoint">
            xValue2 = parseInt("${dataPoint.label}");
            yValue2 = parseFloat("${dataPoint.data}");
            dps2[0].push({
                x: xValue2,
                y: yValue2
            });
        </c:forEach>

        chart2.render();
    }
</script>
