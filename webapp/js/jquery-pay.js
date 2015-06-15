

!function($) {
    "use strict";
    var Modal = function(element, options) {
        this.options = options, this.$element = $(element).delegate('[data-dismiss="modal"]', "click.dismiss.modal", $.proxy(this.hide, this)), 
        this.options.remote && this.$element.find(".modal-body").load(this.options.remote);
    };
    Modal.prototype = {
        constructor: Modal,
        toggle: function() {
            return this[this.isShown ? "hide" : "show"]();
        },
        show: function() {
            var that = this, e = $.Event("show");
            this.$element.trigger(e), this.isShown || e.isDefaultPrevented() || (this.isShown = !0, 
            this.escape(), this.backdrop(function() {
                var transition = $.support.transition && that.$element.hasClass("fade");
                that.$element.parent().length || that.$element.appendTo(document.body), that.$element.show(), 
                transition && that.$element[0].offsetWidth, that.$element.addClass("in").attr("aria-hidden", !1), 
                that.enforceFocus(), transition ? that.$element.one($.support.transition.end, function() {
                    that.$element.focus().trigger("shown");
                }) : that.$element.focus().trigger("shown");
            }));
        },
        hide: function(e) {
            e && e.preventDefault();
            e = $.Event("hide"), this.$element.trigger(e), this.isShown && !e.isDefaultPrevented() && (this.isShown = !1, 
            this.escape(), $(document).unbind("focusin.modal"), this.$element.removeClass("in").attr("aria-hidden", !0), 
            $.support.transition && this.$element.hasClass("fade") ? this.hideWithTransition() : this.hideModal());
        },
        enforceFocus: function() {
            var that = this;
            $(document).bind("focusin.modal", function(e) {
                that.$element[0] === e.target || that.$element.has(e.target).length || that.$element.focus();
            });
        },
        escape: function() {
            var that = this;
            this.isShown && this.options.keyboard ? this.$element.bind("keyup.dismiss.modal", function(e) {
                27 == e.which && that.hide();
            }) : this.isShown || this.$element.unbind("keyup.dismiss.modal");
        },
        hideWithTransition: function() {
            var that = this, timeout = setTimeout(function() {
                that.$element.unbind($.support.transition.end), that.hideModal();
            }, 500);
            this.$element.one($.support.transition.end, function() {
                clearTimeout(timeout), that.hideModal();
            });
        },
        hideModal: function() {
            var that = this;
            this.$element.hide(), this.backdrop(function() {
                that.removeBackdrop(), that.$element.trigger("hidden");
            });
        },
        removeBackdrop: function() {
            this.$backdrop && this.$backdrop.remove(), this.$backdrop = null;
        },
        backdrop: function(callback) {
            var animate = this.$element.hasClass("fade") ? "fade" : "";
            if (this.isShown && this.options.backdrop) {
                var doAnimate = $.support.transition && animate, pageHeight = $(document).height(), pageWidth = "100%";
                if (this.$backdrop = $('<div class="modal-backdrop ' + animate + '" />').appendTo(document.body), 
                this.$backdrop.css({
                    width: pageWidth,
                    height: pageHeight
                }).click("static" == this.options.backdrop ? $.proxy(this.$element[0].focus, this.$element[0]) : $.proxy(this.hide, this)), 
                doAnimate && this.$backdrop[0].offsetWidth, this.$backdrop.addClass("in"), !callback) return;
                doAnimate ? this.$backdrop.one($.support.transition.end, callback) : callback();
            } else !this.isShown && this.$backdrop ? (this.$backdrop.removeClass("in"), $.support.transition && this.$element.hasClass("fade") ? this.$backdrop.one($.support.transition.end, callback) : callback()) : callback && callback();
        }
    };
    var old = $.fn.modal;
    $.fn.modal = function(option) {
        return this.each(function() {
            var $this = $(this), data = $this.data("modal"), options = $.extend({}, $.fn.modal.defaults, $this.data(), "object" == typeof option && option);
            data || $this.data("modal", data = new Modal(this, options)), "string" == typeof option ? data[option]() : options.show && data.show();
        });
    }, $.fn.modal.defaults = {
        backdrop: !0,
        keyboard: !0,
        show: !0
    }, $.fn.modal.Constructor = Modal, $.fn.modal.noConflict = function() {
        return $.fn.modal = old, this;
    }, $(document).bind("click.modal.data-api", '[data-toggle="modal"]', function(e) {
        var $this = $(this), href = $this.attr("href"), $target = $($this.attr("data-target") || href && href.replace(/.*(?=#[^\s]+$)/, "")), option = $target.data("modal") ? "toggle" : $.extend({
            remote: !/#/.test(href) && href
        }, $target.data(), $this.data());
        $target.modal(option).one("hide", function() {
            $this.focus();
        });
    });
}(window.jQuery);

var AntelopePayment = {
    init: function() {
        var a = this;
        $("#payBtn").click(function() {
            var b = a.isSelectedBank(), c = $("#use_cash_account").val(), d = $("#can_pay_all").val(), e = $("#useBalance").attr("data-disabled"), f = a.getBankName();
            
            var retval = ("1" === c ? "true" === e ? ($("#toPayTip").modal("show"), !0) : ("1" === d ? (a.show(), 
                    $("#balancePay").find(".select-other").hide()) : b ? (a.show(), $("#balancePay").find("#bankName").html(f)) : alert("请选择其他支付方式！"), 
                    !1) : b ? void $("#toPayTip").modal("show") : (alert("请选择支付方式"), !1));
            
            if (retval !== false) {
            	$("form").attr("action", window.actionmainurl + "/" + $("input[name='payOnlineBank']:checked").val() + "/1.vot");
            }
            
            return retval;
        }); 
        $("#chooseOther").click(function() {
            $("#balancePay").modal("hide"), $(".bank-title,#bankList").show(), $("#showPaymentAll").trigger("click");
        });
        $("#toPayTipClose").click(function() {
            $("#toPayTip").modal("hide");
        });
        $("#showPaymentAll").click(function() {
            $("#paymentHistory").hide().siblings().fadeIn(100);
            for (var a = $("#paymentHistory").siblings().find("input[name='payOnlineBank']"), b = a.length, c = $("input[name='payOnlineBank']:checked").val(), d = 0; b > d; d += 1) if (a.eq(d).val() === c) {
                a.eq(d).attr("checked", !0);
                break;
            }
            return !1;
        });
        $(".payment-list").find("li").bind("click", function() {
            $(this).find("input").attr("checked", !0);
        });
        $("#alipayTrigger").click(function() {
            return $("#alipay").attr("checked", !0), $("#pay-form").submit(), !1;
        });
        $("#useBalance").click(function() {
            var a = $(this).attr("data-checked"), b = $(this).attr("data-disabled"), c = $("#can_pay_all").val();
            return "true" === b ? !1 : ("false" === a ? ($(this).attr("data-checked", "true"), 
            $("#use_cash_account").val("1"), $(this).addClass("selected"), "0" === c ? $("#titleWrap").animate({
                top: "-50px"
            }, 200, function() {}) : ($(".bank-title").hide(), $("#bankList").hide())) : "true" === a ? ($(this).attr("data-checked", "false"), 
            $("#use_cash_account").val("0"), $(this).removeClass("selected"), $(".bank-title").show(), 
            $("#bankList").show(), "0" === c && $("#titleWrap").animate({
                top: 0
            }, 200, function() {})) : alert("参数错误！"), !1);
        });
        $("#toPay").click(function() {
            a.checkedCode();
        });
    },
    show: function() {
        var a = this;
        a.sendAgain(), $("#balancePay").modal("show");
    },
    isSelectedBank: function() {
        var a = $("input[name='payOnlineBank']:checked").length;
        return 0 >= a ? !1 : !0;
    },
    getBankName: function() {
        return $("input[name='payOnlineBank']:checked").next().clone();
    },
    checkedCode: function() {
        var a = $.trim($("#verifycode").val());
        $.ajax({
            type: "POST",
            url: "/buy/checkCashAccountSms",
            data: "verifycode=" + a + "&orderid=" + order_id,
            dataType: "json",
            async: !1,
            success: function(a) {
                1 === a.code ? ($("#pay-form").submit(), $("#balancePay").modal("hide"), $("#toPayTip").modal("show")) : $("#checkCodeTip").html(a.msg);
            }
        });
    },
    sendAgain: function() {
        var a = this, b = $("#useCashAccountPayLeft").text();
        $.ajax({
            type: "POST",
            url: "/buy/sendCashAccountSms",
            data: "useCashAccountPayLeft=" + b + "&orderid=" + order_id,
            dataType: "json",
            success: function(b) {
                1 === b.code ? a.countdown() : $("#checkCodeTip").html(b.msg);
            }
        });
    },
    countdown: function(a) {
        var b = a || 60, c = null, d = this;
        $("#sendAgain").unbind().find("em").html("(" + b + ")"), c = setInterval(function() {
            b -= 1, b > 0 ? $("#sendAgain").find("em").html("(" + b + ")") : (clearInterval(c), 
            $("#checkCodeTip").html("请重新发送验证码!"), $("#sendAgain").click(function() {
                $("#checkCodeTip").html("已重新发送验证码，请查收!"), d.sendAgain();
            }).find("em").html(""));
        }, 1e3);
    }
};

$(function() {
	AntelopePayment.init();
});
